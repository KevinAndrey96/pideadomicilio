package com.infinitec.pideadomicilio.controller;

import com.infinitec.pideadomicilio.ejb.GeneralTool;
import com.infinitec.pideadomicilio.model.entity.Comercio;
import com.infinitec.pideadomicilio.model.entity.pojo.ResumenBusqueda;
import com.infinitec.pideadomicilio.service.DataService;
import com.mongodb.*;
import org.omnifaces.cdi.ViewScoped;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by romeroej on 1/24/17.
 */

@Named
@ViewScoped
@Transactional
public class ReportBean implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Inject
    private GeneralTool gt;

    @Inject
    private DataService ds;


    @PersistenceContext(unitName = "mongo-ogm")
    EntityManager em;


    @PostConstruct
    public void postConstruct() {
        log.info("Postconstruct " + this.getClass().getSimpleName());

    }


    List<ResumenBusqueda> resumen = new ArrayList<>();
    List<ResumenBusqueda> resumenCategorias = new ArrayList<>();

    List<ResumenBusqueda> resumenZonas = new ArrayList<>();


    public void reportComercio() throws UnknownHostException {


        resumen.clear();
        // Since 2.10.0, uses MongoClient
        MongoClient mongo = new MongoClient("localhost", 27017);


        DB db = mongo.getDB("pedidosadomicilio");

        // get collection
        // if collection doesn't exists, mongodb will create it for you
        DBCollection collection = db.getCollection("Evento");

        String map = "function () {" +
                "emit(this.busqueda, 1);" +
                "}";

        String reduce = "function (key, values) { " +
                "total = Array.sum(values); " +
                " return total} ";
        MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
                null, MapReduceCommand.OutputType.INLINE, null);

        MapReduceOutput out = collection.mapReduce(cmd);




        /*
        for (DBObject o : out.results()) {
           log.info(o.toString());

        }*/


        //HashMap<String,List<ResumenBusqueda>> hashMap = new HashMap<>();


        for (Comercio c : ds.getComercioList()) {
            //hashMap.put(c.getId(),new ArrayList<ResumenBusqueda>());

            log.trace("getting info for " + c.getNombre());
            for (DBObject o : out.results()) {
                if (o.get("_id") == null) {
                    continue;
                }
                log.trace("buscando [nombre] : " + o.get("_id").toString().toUpperCase());
                if (c.getNombre().toUpperCase().startsWith(o.get("_id").toString().toUpperCase())) {
                    ResumenBusqueda rs = new ResumenBusqueda();
                    rs.setComercio(c);
                    rs.setTipo("NOMBRE");
                    rs.setBusqueda(o.get("_id").toString());
                    rs.setCantidad(Float.parseFloat(o.get("value").toString()));
                    rs.setResultado(c.getNombre());

                    //hashMap.get(c.getId()).add(rs);
                    resumen.add(rs);

                    log.trace("Comercio " + c.getNombre() + " se encontro con busqueda [nombre]" + o.get("_id") + " " + o.get("value") + " veces");
                }
                //log.info(o.toString());
            }

            for (String tag : c.getTags()) {
                for (DBObject o : out.results()) {

                    if (o.get("_id") == null) {
                        continue;
                    }

                    log.trace("buscando [tag]: " + o.get("_id").toString().toUpperCase());
                    if (tag.toUpperCase().startsWith(o.get("_id").toString().toUpperCase())) {
                        ResumenBusqueda rs = new ResumenBusqueda();
                        rs.setComercio(c);
                        rs.setTipo("TAG");
                        rs.setBusqueda(o.get("_id").toString());
                        rs.setCantidad(Float.parseFloat(o.get("value").toString()));
                        rs.setResultado(tag);

                        //hashMap.get(c.getId()).add(rs);
                        resumen.add(rs);


                        log.trace("Comercio " + c.getNombre() + " se encontro con busqueda [tag]" + o.get("_id") + " " + o.get("value") + " veces");
                    }
                    //log.info(o.toString());
                }
            }
        }



        /*
        for(List<ResumenBusqueda> list : hashMap.values())
        {
            for(ResumenBusqueda r : list)
            {
                log.info(r.toString());
            }
        }

        for(ResumenBusqueda r : resumen)
        {
            log.info(r.toString());
        }

        */


    }


    public void reportCategorias() throws UnknownHostException {


        resumenCategorias.clear();
        // Since 2.10.0, uses MongoClient
        MongoClient mongo = new MongoClient("localhost", 27017);


        DB db = mongo.getDB("pedidosadomicilio");

        // get collection
        // if collection doesn't exists, mongodb will create it for you
        DBCollection collection = db.getCollection("Evento");

        String map = "function () {" +
                "emit(this.categoria, 1);" +
                "}";

        String reduce = "function (key, values) { " +
                "total = Array.sum(values); " +
                " return total} ";
        MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
                null, MapReduceCommand.OutputType.INLINE, null);

        MapReduceOutput out = collection.mapReduce(cmd);


        for (DBObject o : out.results()) {
            ResumenBusqueda rs = new ResumenBusqueda();
            rs.setComercio(new Comercio());
            rs.getComercio().setNombre("CONSOLIDADO");
            rs.setTipo("CATEGORIA");
            rs.setBusqueda(o.get("_id").toString());
            rs.setCantidad(Float.parseFloat(o.get("value").toString()));
            rs.setResultado(o.get("_id").toString());

            //hashMap.get(c.getId()).add(rs);
            resumenCategorias.add(rs);

        }


        //HashMap<String,List<ResumenBusqueda>> hashMap = new HashMap<>();


        for (Comercio c : ds.getComercioList()) {
            //hashMap.put(c.getId(),new ArrayList<ResumenBusqueda>());

            log.trace("getting info for " + c.getNombre());
            for (DBObject o : out.results()) {
                log.trace("buscando [categoria] : " + o.get("_id").toString().toUpperCase());
                if (c.getCategoria().getNombre().equals(o.get("_id").toString().toUpperCase())) {
                    ResumenBusqueda rs = new ResumenBusqueda();
                    rs.setComercio(c);
                    rs.setTipo("CATEGORIA");
                    rs.setBusqueda(o.get("_id").toString());
                    rs.setCantidad(Float.parseFloat(o.get("value").toString()));
                    rs.setResultado(c.getNombre());

                    //hashMap.get(c.getId()).add(rs);
                    resumenCategorias.add(rs);

                    log.trace("Comercio " + c.getNombre() + " se encontro con busqueda [categoria]" + o.get("_id") + " " + o.get("value") + " veces");
                }
                //log.info(o.toString());
            }


        }



        /*
        for(List<ResumenBusqueda> list : hashMap.values())
        {
            for(ResumenBusqueda r : list)
            {
                log.info(r.toString());
            }
        }

        for(ResumenBusqueda r : resumen)
        {
            log.info(r.toString());
        }

        */


    }

    public void reportZonas() throws UnknownHostException {


        resumenZonas.clear();
        // Since 2.10.0, uses MongoClient
        MongoClient mongo = new MongoClient("localhost", 27017);


        DB db = mongo.getDB("pedidosadomicilio");

        // get collection
        // if collection doesn't exists, mongodb will create it for you
        DBCollection collection = db.getCollection("Evento");

        String map = "function () {" +
                "emit(this.zona, 1);" +
                "}";

        String reduce = "function (key, values) { " +
                "total = Array.sum(values); " +
                " return total} ";
        MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
                null, MapReduceCommand.OutputType.INLINE, null);

        MapReduceOutput out = collection.mapReduce(cmd);


        for (DBObject o : out.results()) {
            ResumenBusqueda rs = new ResumenBusqueda();
            rs.setComercio(new Comercio());
            rs.getComercio().setNombre("CONSOLIDADO");
            rs.setTipo("ZONA");
            rs.setBusqueda(o.get("_id").toString());
            rs.setCantidad(Float.parseFloat(o.get("value").toString()));
            rs.setResultado(o.get("_id").toString());

            //hashMap.get(c.getId()).add(rs);
            resumenZonas.add(rs);

        }


        //HashMap<String,List<ResumenBusqueda>> hashMap = new HashMap<>();


        for (Comercio c : ds.getComercioList()) {
            //hashMap.put(c.getId(),new ArrayList<ResumenBusqueda>());

            log.trace("getting info for " + c.getNombre());
            for (DBObject o : out.results()) {
                log.trace("buscando [zona] : " + o.get("_id").toString().toUpperCase());
                if (c.getZona().getNombreZona().equals(o.get("_id").toString().toUpperCase())) {
                    ResumenBusqueda rs = new ResumenBusqueda();
                    rs.setComercio(c);
                    rs.setTipo("ZONA");
                    rs.setBusqueda(o.get("_id").toString());
                    rs.setCantidad(Float.parseFloat(o.get("value").toString()));
                    rs.setResultado(c.getNombre());

                    //hashMap.get(c.getId()).add(rs);
                    resumenZonas.add(rs);

                    log.trace("Comercio " + c.getNombre() + " se encontro con busqueda [zona]" + o.get("_id") + " " + o.get("value") + " veces");
                }
                //log.info(o.toString());
            }


        }



        /*
        for(List<ResumenBusqueda> list : hashMap.values())
        {
            for(ResumenBusqueda r : list)
            {
                log.info(r.toString());
            }
        }

        for(ResumenBusqueda r : resumen)
        {
            log.info(r.toString());
        }

        */


    }


    //GETTERS


    public List<ResumenBusqueda> getResumenZonas() {
        return resumenZonas;
    }

    public void setResumenZonas(List<ResumenBusqueda> resumenZonas) {
        this.resumenZonas = resumenZonas;
    }

    public List<ResumenBusqueda> getResumen() {
        return resumen;
    }

    public void setResumen(List<ResumenBusqueda> resumen) {
        this.resumen = resumen;
    }

    public List<ResumenBusqueda> getResumenCategorias() {
        return resumenCategorias;
    }

    public void setResumenCategorias(List<ResumenBusqueda> resumenCategorias) {
        this.resumenCategorias = resumenCategorias;
    }

    private Object filteredZona;
    private Object filteredCategoria;
    private Object filteredComercio;

    public Object getFilteredZona() {
        return filteredZona;
    }

    public void setFilteredZona(Object filteredZona) {
        this.filteredZona = filteredZona;
    }

    public Object getFilteredCategoria() {
        return filteredCategoria;
    }

    public void setFilteredCategoria(Object filteredCategoria) {
        this.filteredCategoria = filteredCategoria;
    }

    public Object getFilteredComercio() {
        return filteredComercio;
    }

    public void setFilteredComercio(Object filteredComercio) {
        this.filteredComercio = filteredComercio;
    }
}

