package com.infinitec.pideadomicilio.controller;

import com.infinitec.pideadomicilio.ejb.GeneralTool;
import com.infinitec.pideadomicilio.model.entity.Categoria;
import com.infinitec.pideadomicilio.model.entity.Ciudad;
import com.infinitec.pideadomicilio.model.entity.Comercio;
import com.infinitec.pideadomicilio.model.entity.Zona;
import com.infinitec.pideadomicilio.service.DataService;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by romeroej on 1/24/17.
 */

@Named
@ViewScoped
public class SearchBean implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Inject
    private GeneralTool gt;

    @Inject
    private DataService ds;


    @PersistenceContext(unitName = "mongo-ogm")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        log.info("Postconstruct " + this.getClass().getSimpleName());


    }


    public void handleSelect(SelectEvent event) {
        Object item = event.getObject();
        log.trace("Selected Item:" + item);
    }

    public void handleSelectCiudad(SelectEvent event) {
        Object item = event.getObject();
        log.trace("Selected Item:" + item);
        this.zona = null;
    }



    public List<String> completeTextComercio(String query) {
        List<String> results = new ArrayList<String>();

        ds.getComercioList().stream().filter(c -> {
            boolean filtered = false;
            if(this.zona != null)
            {

                if(c.getZona().equals(this.zona))
                {
                    filtered = true;
                }else
                    return false;
            }

            if(this.categoria != null)
            {
                        if(c.getCategoria().equals(this.categoria))
                        {
                            filtered = true;
                        }else
                            return false;
            }


           return  c.getNombre().toUpperCase().contains(query.toUpperCase());


        }
        ).forEach(c -> results.add(c.getNombre()));




        Set<String> hs = new HashSet<>();
        hs.addAll(results);
        results.clear();
        results.addAll(hs);


        return results;
    }

    public List<Zona> completeTextZonas(String query) {
        List<Zona> results = new ArrayList<Zona>();


        ds.getZonaList().stream().filter(c -> {
            String fz = c.getNombre().toUpperCase();
            return (fz.contains(query.toUpperCase()) && c.getCiudad().equals(ciudad));
        }).forEach(c -> results.add(c));


        return results.stream().sorted((f1, f2) -> f1.getNombre().compareTo(f2.getNombre())).collect(Collectors.toList());


    }


    public void doSearch() {


        log.trace("Searching " + dataStr);
        comercioList.clear();
        banners.clear();
        bannersResp.clear();


        if (zona == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se pueden realizar busquedas sin zona", ""));
            log.info("No se pueden realizar busquedas sin zonas.");
            return;
        }

        sendEvent();

        if (categoria != null) {

            //populate banners wit the section

            ds.getBannerList().stream().filter(c -> c.getCategoria().equals(categoria)).sorted(Comparator.comparing(n -> n.getPeso())).forEach(n -> banners.add(n.getImg()));
            ds.getBannerList().stream().filter(c -> c.getCategoria().equals(categoria)).sorted(Comparator.comparing(n -> n.getPeso())).forEach(n -> bannersResp.add(n.getImgResp()));

            String queryText = "";

            if (dataStr == null || dataStr.isEmpty())
                queryText = "{$query: {$and: [{zona_id:'" + zona.getId() + "'},{categoria_id:'" + categoria.getId() + "'} ]} }";
            else
                queryText = "{$query: {$and: [{ nombre: {$regex : '" + dataStr + "',$options:'i'  }},{zona_id:'" + zona.getId() + "'},{categoria_id:'" + categoria.getId() + "'} ]} }";

            log.trace("Query x Nombre/Zona/Categoria =" + queryText);
            List<Comercio> queryTags = em.createNativeQuery(queryText, Comercio.class).getResultList();

            log.trace(String.format("Se encontraron %s registros para la busqueda x nombre %s y zona %s", queryTags.size(), dataStr, zona.getId()));
            comercioList.addAll(queryTags);


            //queryText = "{$query: {$and: [{ tags : { $all : [  {\"$elemMatch\": {tags: {$regex : '" + dataStr + "',$options:'i' } }}   ]} },{zona_id:'" + zona.getId() + "'},{categoria_id:'" + categoria.getId() + "'} ]} }";

            if (dataStr == null || dataStr.isEmpty()) {
                //queryText = "{$query: {$and: [{zona_id:'" + zona.getId() + "' },{categoria_id:'"+categoria.getId()+"'}]}}";

                log.info("Everything fetch by name");
            } else {
                queryText = "{$query: {$and: [{tags: { $regex:'" + dataStr + "',$options:'i'}},{zona_id:'" + zona.getId() + "' },{categoria_id:'" + categoria.getId() + "'}]}}";

                log.trace("Query x Tag/Zona/Categoria =" + queryText);

                queryTags = em.createNativeQuery(queryText, Comercio.class).getResultList();

                log.trace(String.format("Se encontraron %s registros para la busqueda x tag %s y zona %s", queryTags.size(), dataStr, zona.getId()));
                comercioList.addAll(queryTags);

            }
            Set<Comercio> hs = new HashSet<>();
            hs.addAll(comercioList);
            comercioList.clear();
            comercioList.addAll(hs);

            log.info(String.format("Se encontraron %s registros para la busqueda", comercioList.size()));


        } else {


            String queryText = "";

            if (dataStr == null || dataStr.isEmpty())
                queryText = "{$query: {$and: [{zona_id:'" + zona.getId() + "'} ]} }";
            else
                queryText = "{$query: {$and: [{ nombre: {$regex : '" + dataStr + "',$options:'i'  }},{zona_id:'" + zona.getId() + "'} ]} }";


            log.info("Query x Nombre y Zona =" + queryText);
            List<Comercio> queryTags = em.createNativeQuery(queryText, Comercio.class).getResultList();

            log.trace(String.format("Se encontraron %s registros para la busqueda x nombre %s y zona %s", queryTags.size(), dataStr, zona.getId()));
            comercioList.addAll(queryTags);


            //db.getCollection('Comercio').find( {zona_id:'6bc7746a-5aa5-4840-83a9-ccc2710d3e19',tags: { $regex:'inf',$options:'i'}  })


            if (dataStr == null || dataStr.isEmpty()) {
                log.info("Skipping tag search, already found everything");
            } else {

                queryText = "{$query: {$and: [ {tags: { $regex:'" + dataStr + "',$options:'i'}},{zona_id:'" + zona.getId() + "' }]}}";

                log.trace("Query x Tag y Zona =" + queryText);

                queryTags = em.createNativeQuery(queryText, Comercio.class).getResultList();

                log.trace(String.format("Se encontraron %s registros para la busqueda x tag %s y zona %s", queryTags.size(), dataStr, zona.getId()));
                comercioList.addAll(queryTags);
            }


            Set<Comercio> hs = new HashSet<>();
            hs.addAll(comercioList);
            comercioList.clear();
            comercioList.addAll(hs);

            log.info(String.format("Se encontraron %s registros para la busqueda", comercioList.size()));


        }


        Collections.sort(comercioList, new Comparator<Comercio>() {
            @Override
            public int compare(Comercio lhs, Comercio rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getPeso() == rhs.getPeso() ? 0 : lhs.getPeso() > rhs.getPeso() ? 1 : -1;
            }
        });


        if (comercioList.size() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se encontraron comercios para la busqueda", ""));
            log.info("No se encontraron comercios para la busqueda.");
            return;
        }
        //


    }


    public void sendEvent() {

        if (dataStr != null && dataStr.length() < 3) {
            log.info("ignore event <3");
            return;
        }

        log.trace("loggin search event");

        //to DB
        gt.saveEvento(zona, categoria, dataStr);


        //to GA
        String evento = "ga('send', 'event', 'zona', 'busqueda', '" + zona.getNombreZona() + "');";
        //RequestContext.getCurrentInstance().execute(evento);

        if (categoria != null) {
            evento = evento + "ga('send', 'event', 'categoria', 'busqueda', '" + categoria.getNombre() + "');";
            RequestContext.getCurrentInstance().execute(evento);
        }

        evento = evento + "ga('send', 'event', 'busqueda', 'busqueda', '" + dataStr + "');";
        RequestContext.getCurrentInstance().execute(evento);
    }


    private Zona zona = null;
    private Ciudad ciudad = null;
    private Categoria categoria = null;
    private String dataStr = "";

    private List<Comercio> comercioList = new ArrayList<>();
    private List<String> banners = new ArrayList<>();

    private List<String> bannersResp = new ArrayList<>();


    //GETTER SETTERs

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }


    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public List<Comercio> getComercioList() {
        return comercioList;
    }

    public void setComercioList(List<Comercio> comercioList) {
        this.comercioList = comercioList;
    }

    public List<String> getBanners() {
        return banners;
    }

    public void setBanners(List<String> banners) {
        this.banners = banners;
    }

    public List<String> getBannersResp() {
        return bannersResp;
    }

    public void setBannersResp(List<String> bannersResp) {
        this.bannersResp = bannersResp;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
}

