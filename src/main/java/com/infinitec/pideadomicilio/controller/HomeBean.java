package com.infinitec.pideadomicilio.controller;

import com.infinitec.pideadomicilio.ejb.GeneralTool;
import com.infinitec.pideadomicilio.model.entity.*;
import com.infinitec.pideadomicilio.model.entity.pojo.ResumenBusqueda;
import com.infinitec.pideadomicilio.service.DataService;
import com.infinitec.pideadomicilio.tools.URLParamEncoder;
import com.mongodb.*;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.*;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by romeroej on 1/24/17.
 */

@Named
@ViewScoped
@Transactional
public class HomeBean implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Inject
    private GeneralTool gt;

    @Inject
    private DataService ds;


    @PostConstruct
    public void postConstruct() {
        log.info("Postconstruct " + this.getClass().getSimpleName());
        //ds.reload();

    }


    //banner y comercio
    public void remove(Object obj) {
        gt.remove(obj);
        ds.reload();
    }

    public void removeCategoria(Object obj) {
        //revisar si algun banner usa categoria o si algun producto usa la categoria
        if (ds.getBannerList().stream().filter(c -> c.getCategoria().equals(obj)).count() == 0 && ds.getComercioList().stream().filter(c -> c.getCategoria().equals(categoria)).count() == 0) {
            remove(obj);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede borrar el objeto, porque es usado por un comercio o banner", ""));

            return;
        }

    }

    public void removeZona(Object obj) {
        //revisar si algun comercio usa la zona.
        if (ds.getComercioList().stream().filter(c -> c.getZona().equals(obj)).count() == 0) {
            remove(obj);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede borrar el objeto, porque es usado por un comercio", ""));

            return;
        }


    }


    public void removeCiudad(Object obj) {
        //revisar si algun comercio usa la zona.
        if (ds.getZonaList().stream().filter(c -> c.getCiudad().equals(obj)).count() == 0) {
            remove(obj);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede borrar el objeto, porque es usado por una zona", ""));

            return;
        }
    }


    private Ciudad ciudad = null;
    private Zona zona = null;
    private Comercio comercio = null;
    private Categoria categoria = null;
    private String dataStr = "";
    private Banner banner;
    private boolean merge = false;


    public void nuevaCiudad() {
        ciudad = new Ciudad();
    }

    public void guardarCiudad() {
        if(merge == false)
        {
            ciudad.setId(ciudad.getNombre());
        }

        gt.persist(ciudad, merge);
        ds.reload();
        ciudad = null;
        merge = false;

    }


    public void nuevaZona() {
        zona = new Zona();
    }

    public void guardarZona() {

        if(merge == false)
        {
            zona.setId(zona.getNombre()+"_"+zona.getCiudad().getNombre());
        }

        gt.persist(zona, merge);
        ds.reload();
        zona = null;
        merge = false;
    }

    public void nuevoComercio() {
        comercio = new Comercio();
        comercio.setTelefonos(new HashSet<>());
        comercio.setTags(new HashSet<>());
    }

    public void nuevoBanner() {
        banner = new Banner();
    }


    public void guardarBanner() {
        gt.persist(banner, merge);
        ds.reload();
        banner = null;
        filename = "";
        uploadedFile = null;
        merge = false;
    }


    public void guardarComercio() {
        gt.persist(comercio, merge);
        ds.reload();
        comercio = null;
        filename = "";
        uploadedFile = null;
        merge = false;
    }


    public void nuevaCategoria() {
        categoria = new Categoria();
    }

    public void guardarCategoria() {
        if(merge == false)
        {
            categoria.setId(categoria.getNombre());
        }

        gt.persist(categoria, merge);
        ds.reload();
        categoria = null;
        merge = false;
    }

    public void addTel() {
        if (dataStr.contains(",")) {
            comercio.getTelefonos().addAll(Arrays.asList(dataStr.split(",")));
        } else {
            comercio.getTelefonos().add(dataStr);
        }
        dataStr = "";
    }

    public void editComercio(Comercio com) {
        comercio = com;
        merge = true;
    }

    public void editCiudad(Ciudad ciu) {
        ciudad = ciu;
        merge = true;
    }


    public void editCategoria(Categoria cat) {
        categoria = cat;
        merge = true;
    }


    public void editZona(Zona zona1) {
        zona = zona1;
        merge = true;
    }

    public void editBanners(Banner ban) {
        banner = ban;
        merge = true;
    }


    public void removeTel(String string) {
        comercio.getTelefonos().remove(string);
    }

    public void addTag() {

        if (dataStr.contains(",")) {
            comercio.getTags().addAll(Arrays.asList(dataStr.split(",")));
        } else {

            comercio.getTags().add(dataStr);
        }

        dataStr = "";
    }


    private InputStream uploadedFile = null;
    private String filename;


    public InputStream getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(InputStream uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        log.info("Succesful", event.getFile().getFileName() + " is uploaded.");


        uploadedFile = event.getFile().getInputstream();
        filename = event.getFile().getFileName();


        String pathToSave = "/opt/filespad/";
        //pathToSave = "/tmp/";


        //String fname = "temp" + "."+ filename.split("\\.")[1];
        String fname = URLParamEncoder.encode(filename);


        ByteArrayOutputStream baos = gt.copyIS(uploadedFile);


        uploadedFile = new ByteArrayInputStream(baos.toByteArray());
        InputStream copyStream = new ByteArrayInputStream(baos.toByteArray());


        gt.saveFileToPath(copyStream, pathToSave, filename, false);

        if (comercio != null) {
            comercio.setImg("" + fname);
            log.info("path " + comercio.getImg());

        }

        if (banner != null) {
            banner.setImg("/file/" + fname);
            log.info("path " + banner.getImg());
        }

        //String pathToImg = "http://127.0.0.1:8080/" + "/file/" + fname;
        //filename = pathToImg;


    }



    public void handleFileUpload2(FileUploadEvent event) throws IOException {
        log.info("Succesful", event.getFile().getFileName() + " is uploaded.");


        uploadedFile = event.getFile().getInputstream();
        filename = event.getFile().getFileName();


        String pathToSave = "/opt/filespad/";

        //String fname = "temp" + "."+ filename.split("\\.")[1];
        String fname = URLParamEncoder.encode(filename);


        ByteArrayOutputStream baos = gt.copyIS(uploadedFile);


        uploadedFile = new ByteArrayInputStream(baos.toByteArray());
        InputStream copyStream = new ByteArrayInputStream(baos.toByteArray());


        gt.saveFileToPath(copyStream, pathToSave, filename, false);

        if (comercio != null) {
            comercio.setImg("" + fname);
            log.info("path " + comercio.getImg());

        }

        if (banner != null) {
            banner.setImgResp("/file/" + fname);
            log.info("path " + banner.getImg());
        }

        //String pathToImg = "http://127.0.0.1:8080/" + "/file/" + fname;
        //filename = pathToImg;


    }


    public void removeTag(String string) {
        comercio.getTags().remove(string);
    }
    //GETTER SETTERs


    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
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

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }


    private Object filteredComercio;

    public Object getFilteredComercio() {
        return filteredComercio;
    }

    public void setFilteredComercio(Object filteredComercio) {
        this.filteredComercio = filteredComercio;
    }
}

