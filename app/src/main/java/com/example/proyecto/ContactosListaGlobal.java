package com.example.proyecto;

import java.util.ArrayList;

import POJO.contactos;

public class ContactosListaGlobal {
    private static ContactosListaGlobal instancia;
    private ArrayList<contactos> listaContactos;

    private ContactosListaGlobal() {
        listaContactos = new ArrayList<>();
    }

    public static synchronized ContactosListaGlobal getInstancia() {
        if (instancia == null) {
            instancia = new ContactosListaGlobal();
        }
        return instancia;
    }

    public ArrayList<contactos> getListaContactos() {
        return listaContactos;
    }

    public void agregarContacto(contactos contacto) {
        if (!listaContactos.contains(contacto)) {
            listaContactos.add(contacto);
        }
    }
    public ArrayList<contactos> obtenerContactos() {
        return new ArrayList<>(listaContactos);
    }
    public void eliminarContacto(contactos contacto)
    {
        listaContactos.remove(contacto);
    }
}

