package com.example.l2_gtics.controller;


import com.example.l2_gtics.entity.Coordenada;
import com.example.l2_gtics.entity.Parametros;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    static int turno = 1;
    static Parametros parametros;

    static int filaExplotar = 0;
    static int columnaExplotar = 0;
    static List<Coordenada> listaBombas= new ArrayList<>();

    static Coordenada[][] tablero;



    @GetMapping(value ={"/buscaminas","","/"})
    public String configurarBuscaminas(){
        return "buscaminas";
    }

    @PostMapping(value = {"/buscaminas/guardar","/guardar","guardar"} )
    public String guardarParametros(Model model,  Parametros parametrosGuardar){


        parametros = parametrosGuardar;

        System.out.println(parametros.getFilas());
        System.out.println(parametros.getColumnas());


        return "redirect:/jugar";
    }


    @GetMapping(value ={"/jugar"})
    public String jugarBuscaminas( Model model){

        String text = parametros.getPosiciones();
        String[] partes = text.split(" ");

        for(String subpartes : partes){
            Coordenada coordenada = new Coordenada();
            coordenada.setFila(Character.getNumericValue(subpartes.charAt(1)));
            coordenada.setColumna(Character.getNumericValue(subpartes.charAt(3)));
            listaBombas.add(coordenada);
        }


        //Inicializar tablero
        tablero =  new Coordenada[parametros.getFilas()][parametros.getColumnas()];

        for(int i =1; i<= parametros.getFilas(); i++){
            for(int j =1; j<= parametros.getColumnas(); j++){
                Coordenada coordenada = new Coordenada();
                coordenada.setFila(i);
                coordenada.setColumna(j);
                coordenada.setEstadoCasilla(1);
                coordenada.setNumBombasAlrededor(calcularNumBombasAlrededor(i,j,listaBombas));
                tablero[i-1][j-1] = coordenada;
            }
        }



        model.addAttribute("parametros",parametros);
        model.addAttribute("turno",turno);
        model.addAttribute("tablero",tablero);
        return "jugar";
    }


    @GetMapping(value ={"/minar"})
    public String jugarBuscaminasMinar(Model model){

        boolean ganar=false;
        boolean perder=false;

        if(turno>=2){
            actualizarTablero(filaExplotar,columnaExplotar);
            ganar = win();
            if(parametros.getIntentos()==0){
                perder = true;
            }
        }

        model.addAttribute("perder",perder);
        model.addAttribute("ganar",ganar);
        model.addAttribute("parametros",parametros);
        model.addAttribute("turno",turno);
        model.addAttribute("tablero",tablero);

        return "jugar";
    }





    @PostMapping(value = {"/jugar/guardarcoordenada"} )
    public String guardarCoordenada(Model model, @RequestParam("coordenada") String coordenada){

        String[] partes = coordenada.split(" ");
        filaExplotar = Integer.parseInt(partes[0]);
        columnaExplotar = Integer.parseInt(partes[1]);
        turno++;

        return "redirect:/minar";
    }


    public int calcularNumBombasAlrededor(int fila, int columna, List<Coordenada> listaBombas) {

        int count = 0;

        for(Coordenada c: listaBombas){
            if(fila-1 == c.getFila() && columna-1 == c.getColumna()){
                count ++;
            }
        }
        for(Coordenada c: listaBombas){
            if(fila-1 == c.getFila() && columna == c.getColumna()){
                count ++;
            }
        }
        for(Coordenada c: listaBombas){
            if(fila-1 == c.getFila() && columna+1 == c.getColumna()){
                count ++;
            }
        }
        for(Coordenada c: listaBombas){
            if(fila == c.getFila() && columna-1 == c.getColumna()){
                count ++;
            }
        }
        for(Coordenada c: listaBombas){
            if(fila == c.getFila() && columna+1 == c.getColumna()){
                count ++;
            }
        }
        for(Coordenada c: listaBombas){
            if(fila+1 == c.getFila() && columna-1 == c.getColumna()){
                count ++;
            }
        }
        for(Coordenada c: listaBombas){
            if(fila+1 == c.getFila() && columna == c.getColumna()){
                count ++;
            }
        }
        for(Coordenada c: listaBombas){
            if(fila+1 == c.getFila() && columna+1 == c.getColumna()){
                count ++;
            }
        }

        return count;
    }

    public void actualizarTablero(int filaExplotar, int columnaExplotar) {

        if(filaExplotar>=1 && filaExplotar <= parametros.getFilas() && columnaExplotar>=1 && columnaExplotar <= parametros.getColumnas() && tablero[filaExplotar-1][columnaExplotar-1].getEstadoCasilla() == 1){

            if(esBomba(filaExplotar,columnaExplotar)){
                tablero[filaExplotar-1][columnaExplotar-1].setEstadoCasilla(4);
                parametros.setIntentos(parametros.getIntentos()-1);
            }
            else {

                if(tablero[filaExplotar-1][columnaExplotar-1].getNumBombasAlrededor() == 0)
                {
                    tablero[filaExplotar-1][columnaExplotar-1].setEstadoCasilla(2);

                    actualizarTablero(filaExplotar-1,columnaExplotar-1);
                    actualizarTablero(filaExplotar-1,columnaExplotar);
                    actualizarTablero(filaExplotar-1,columnaExplotar+1);
                    actualizarTablero(filaExplotar,columnaExplotar-1);
                    actualizarTablero(filaExplotar,columnaExplotar+1);
                    actualizarTablero(filaExplotar+1,columnaExplotar-1);
                    actualizarTablero(filaExplotar+1,columnaExplotar);
                    actualizarTablero(filaExplotar+1,columnaExplotar+1);

                }

                if(tablero[filaExplotar-1][columnaExplotar-1].getNumBombasAlrededor() != 0)
                {
                    tablero[filaExplotar-1][columnaExplotar-1].setEstadoCasilla(3);

                }

            }



        }

    }

    public boolean esBomba(int filaExplotar, int columnaExplotar) {

        for(Coordenada c: listaBombas){
            if(filaExplotar == c.getFila() && columnaExplotar == c.getColumna()){
                return true;
            }
        }
        return false;

    }


    public boolean win() {
        int count = 0;
        boolean win = false;

        for(int i =1; i<= parametros.getFilas(); i++){
            for(int j =1; j<= parametros.getColumnas(); j++){

                if(tablero[i-1][j-1].getEstadoCasilla() == 1) {
                    count++;
                }
            }
        }

        if(count == 0 && parametros.getIntentos()>=1){
            win = true;
        }

        return win;

    }






}
