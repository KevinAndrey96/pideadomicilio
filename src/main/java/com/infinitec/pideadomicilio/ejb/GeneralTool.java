package com.infinitec.pideadomicilio.ejb;

import com.infinitec.pideadomicilio.account.ExtendedCredentials;
import com.infinitec.pideadomicilio.model.entity.Categoria;
import com.infinitec.pideadomicilio.model.entity.Evento;
import com.infinitec.pideadomicilio.model.entity.User;
import com.infinitec.pideadomicilio.model.entity.Zona;
import com.infinitec.pideadomicilio.tools.Encryptor;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.*;
import java.util.Date;

/**
 * Created by romeroej on 1/25/17.
 */

@Stateless
public class GeneralTool implements Serializable {


    private static final long serialVersionUID = 1L;


    @PersistenceContext(unitName = "mongo-ogm")
    private EntityManager em;

    @Inject
    private Logger log;





    @PostConstruct
    public void postConstruct() {
        log.info("Postconstruct " + this.getClass().getSimpleName());

    }



    public String encrypt(String password) {
        String key = "Inf12345Inf12345"; // 128 bit key
        String initVector = "RandomInftVector"; // 16 bytes IV
        String encryptedStr = Encryptor.encrypt(key, initVector, password);
        return encryptedStr;
    }


    public User createUser(User user, boolean persist) {

        user.setUsername(user.getUsername().toUpperCase());
        user.setPassword(this.encrypt(user.getPassword()));
        this.persist(user, persist);
        return user;
    }



    public User changePwd(String userStr,String password,String newPass) {

        User user = validateUser(userStr,password);

        if(user != null)
        {
            user.setPassword(this.encrypt(newPass));
            this.persist(user,true);
            return user;
        }else
        {
            return null;
        }

    }

    public User validateUser(String userStr,String password) {
        String queryText = "{$query: {$and: [{ _id:'" + userStr + "' },{password:'" + this.encrypt(password) + "'} ]} }";
        log.trace("Query  Por Login =" + queryText);
        User user = null;


        try {
            user = (User) em.createNativeQuery(queryText, User.class).getSingleResult();
        } catch (Exception e) {

        }


        return user;

    }



    public User validateUser(ExtendedCredentials credentials) {
        String queryText = "{$query: {$and: [{ _id:'" + credentials.getUsername() + "' },{password:'" + this.encrypt(credentials.getPassword()) + "'} ]} }";
        log.trace("Query  Por Login =" + queryText);
        User user = null;


        try {
            user = (User) em.createNativeQuery(queryText, User.class).getSingleResult();
        } catch (Exception e) {

        }


        return user;

    }


    @Transactional
    public void remove(Object obj) {
        obj = em.merge(obj);
        em.remove(obj);


    }

    @Transactional
    public void persist(Object obj, boolean merge) {


        if (merge)
            em.merge(obj);
        else
            em.persist(obj);
    }



    public void saveEvento(Zona zona, Categoria categoria, String termino)
    {
        Evento evento = new Evento();
        evento.setTipo("GENERICO");
        evento.setFecha(new Date());
        evento.setBusqueda(termino);
        evento.setZona(zona.getNombreZona());
        evento.setCategoria(categoria==null?"":categoria.getNombre());

        em.persist(evento);
    }

    public ByteArrayOutputStream copyIS(InputStream stream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        try {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = stream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (
                Exception ex
                ) {

        }

        return baos;

    }

    public void saveFileToPath(InputStream inputStream, String path, String fname, boolean closeIS) {

        OutputStream outputStream = null;

        try {
            // read this file into InputStream

            // write the inputStream to a FileOutputStream
            outputStream =
                    new FileOutputStream(new File(path + fname));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            log.info("File Saved!");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null && closeIS) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
