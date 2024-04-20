package com.example.l2_gtics.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordenada {

    private int fila;
    private int columna;
    private int numBombasAlrededor;
    private int estadoCasilla;

    //Estados de casilla:
    // 1: Sin descubrir
    // 2: Descubierta y sin bombas alrededor
    // 3: Descubierta y con bombas alrededor
    // 4: Descubierta y con una bomba en esa coordenada

}
