package com.infinitec.pideadomicilio.service;

/**
 * Created by romeroej on 1/25/17.
 */

import com.infinitec.pideadomicilio.model.entity.*;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Named
@ApplicationScoped
public class DataService implements Serializable {

    List<Ciudad> ciudadList;
    List<Zona> zonaList;
    List<Comercio> comercioList;
    List<Categoria> categoriaList;
    List<Banner> bannerList;

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;


    @PersistenceContext(unitName = "mongo-ogm")
    private EntityManager em;

    @PostConstruct
    public void init() {

        log.info("Dataservices Init");
        reload();
    }


    public void reload()
    {
        log.info("Reloading Data Service");
        fetchCiudades();
        fetchZonas();
        fetchComercios();
        fetchCategorias();
        fetchBanners();

    }


    public void fetchBanners() {
        Query query = em
                .createQuery("from Banner ");

        bannerList = query.getResultList();
        log.info(String.format("%s banners en catalogo", bannerList.size()));

    }


    public void fetchCategorias() {
        Query query = em
                .createQuery("from Categoria ");

        categoriaList = query.getResultList();
        log.info(String.format("%s categoria en catalogo", categoriaList.size()));

    }

    public void fetchCiudades() {
        Query query = em
                .createQuery("from Ciudad ");

        ciudadList = query.getResultList();
        log.info(String.format("%s ciudades en catalogo", ciudadList.size()));

    }


    public void fetchZonas() {
        Query query = em
                .createQuery("from Zona ");

        zonaList = query.getResultList();
        log.info(String.format("%s zonas en catalogo", zonaList.size()));


    }


    public void fetchComercios() {
        Query query = em
                .createQuery("from Comercio ");

        comercioList = query.getResultList();

    }

    public List<Ciudad> getCiudadList() {
        return ciudadList;
    }

    public List<Zona> getZonaList() {
        return zonaList;
    }

    public List<Comercio> getComercioList() {
        return comercioList;
    }

    public List<Categoria> getCategoriaList() {
        return categoriaList;
    }

    public List<Ciudad> completeTextCiudades(String query) {
        List<Ciudad> results = new ArrayList<Ciudad>();

        ciudadList.stream().filter(c -> c.getNombre().toUpperCase().startsWith(query.toUpperCase())).forEach(c -> results.add(c));



        return results.stream().sorted((f1, f2) -> f1.getNombre().compareTo(f2.getNombre())).collect(Collectors.toList());
    }


    public List<Zona> completeTextZonas(String query) {
        List<Zona> results = new ArrayList<Zona>();

        zonaList.stream().filter(c -> {
            String fz =  c.getNombre().toUpperCase();
            return fz.contains(query.toUpperCase()) ;
        }).forEach(c -> results.add(c));


        return results.stream().sorted((f1, f2) -> f1.getNombre().compareTo(f2.getNombre())).collect(Collectors.toList());
    }

    public List<Categoria> completeTextCategoria(String query) {
        List<Categoria> results = new ArrayList<Categoria>();

        categoriaList.stream().filter(c -> {
            return c.getNombre().toUpperCase().contains(query.toUpperCase());
        }).forEach(c -> results.add(c));


        return results.stream().sorted((f1, f2) -> f1.getNombre().compareTo(f2.getNombre())).collect(Collectors.toList());
    }


    public List<String> completeTextComercio(String query) {
        List<String> results = new ArrayList<String>();

        comercioList.stream().filter(c -> {
            return c.getNombre().toUpperCase().contains(query.toUpperCase());
        }).forEach(c -> results.add(c.getNombre()));




        Set<String> hs = new HashSet<>();
        hs.addAll(results);
        results.clear();
        results.addAll(hs);


        return results;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }


}