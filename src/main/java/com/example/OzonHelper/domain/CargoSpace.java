package com.example.OzonHelper.domain;

import lombok.Data;

@Data
public class CargoSpace {
    private int boxes;
    private int pallets;

    public CargoSpace(int pallets, int boxes) {
        if (boxes < 0 || pallets < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным.");
        }
        this.pallets = pallets;
        this.boxes = boxes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (boxes != 0) {
            sb.append(boxes);
            sb.append(" коробок ");
        }
        if (pallets != 0) {
            sb.append(pallets);
            sb.append(" паллет");
        }
        return sb.toString();
    }

    public int getTotal() {
        return boxes + pallets;
    }
}
