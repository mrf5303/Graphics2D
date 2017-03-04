/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics2d;


import java.awt.*;
import java.awt.Graphics2D;

/**
 *
 * @author Matthew
 */
public class ItemstoDraw {

    private String shape;
    private Paint paint;
    private boolean isFilled;
    private BasicStroke stroke;

    public ItemstoDraw(String s, Paint p, boolean f, BasicStroke st) {
        shape = s;
        paint = p;
        isFilled = f;
        stroke = st;

    }

    public void paint(Graphics2D g2d, int[] x, int[] y) {
        g2d.setPaint(paint);
        g2d.setStroke(stroke);
        switch (shape) {
            case "Rectangle":
                if (isFilled == true) {
                    g2d.fillRect(x[0], y[0], x[1] - x[0], y[1] - y[0]);

                } else {

                    g2d.drawRect(x[0], y[0], x[1] - x[0], y[1] - y[0]);
                }
                break;
            case "Oval":
                if (isFilled == true) {
                    g2d.fillOval(x[0], y[0], x[1] - x[0], y[1] - y[0]);
                } else {

                    g2d.drawOval(x[0], y[0], x[1] - x[0], y[1] - y[0]);
                }
                break;
            case "Line":

                g2d.drawLine(x[0], y[0], x[1], y[1]);
                break;

        }

    }

}
