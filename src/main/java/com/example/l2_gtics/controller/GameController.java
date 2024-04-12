package com.example.l2_gtics.controller;


import com.example.l2_gtics.entity.Parametros;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {



    @GetMapping(value ={"/buscaminas","","/"})
    public String configurarBuscaminas(){
        return "buscaminas";
    }

    @PostMapping(value = {"/buscaminas/guardar","/guardar","guardar"} )
    public String guardarParametros(Model model, Parametros parametros){
        model.addAttribute("parametros",parametros);

        System.out.println(parametros.getFilas());
        System.out.println(parametros.getColumnas());


        return "redirect:/jugar";
    }


    @GetMapping(value ={"/jugar"})
    public String jugarBuscaminas(Model model){

        model.addAttribute("funciona","este es un mensaje");
        ArrayList<Integer> listaFilas = new ArrayList<>();
        for(int i=0;i< 5;i=i+1){
            listaFilas.add(i);
        }
        model.addAttribute("listaFilas",listaFilas);

        ArrayList<Integer> listaColumnas = new ArrayList<>();
        for(int i=0;i< 6;i=i+1){
            listaColumnas.add(i);
        }
        model.addAttribute("listaColumnas",listaColumnas);

        return "jugar";
    }

    @PostMapping(value = {"/jugar/guardarcoordenada"} )
    public String guardarCoordenada(Model model, @RequestParam("coordenada") String coordenada){

        String[] partes = coordenada.split(" ");
        int fila = Integer.parseInt(partes[0]);
        int columna = Integer.parseInt(partes[1]);

        model.addAttribute("filaExplotar",fila);
        model.addAttribute("columnaExplotar",columna);



        return "redirect:/jugar";
    }

}
