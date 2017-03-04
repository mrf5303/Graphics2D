/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Matthew
 */
public class GraphicsFrame extends JFrame {

    private Color c1 = Color.LIGHT_GRAY;
    private Color c2 = Color.LIGHT_GRAY;
    private DrawPanel canvas = new DrawPanel();
    private JCheckBox filled = new JCheckBox("Filled");
    private JCheckBox UseGradient = new JCheckBox("Use Gradient");
    private JLabel location = new JLabel();
    private JComboBox<String> shapes;
    private JTextField width = new JTextField(2);
    private JTextField length = new JTextField(2);
    private JCheckBox dashed = new JCheckBox("Dashed");
    private int[] x = new int[2];
    private int[] y = new int[2];
    private Image image;
    private Graphics2D g2d;
    private Paint p1 = c1;
    private ArrayList<ItemstoDraw> items = new ArrayList<>();
    private int erase = 0;

    public GraphicsFrame() {

        //setting up interface
        super("Graphics2D");
        JButton undo = new JButton("Undo");
        JButton clear = new JButton("Clear");
        JLabel l1 = new JLabel("Shape:");
        String[] names = {"Oval", "Rectangle", "Line"};
        shapes = new JComboBox(names);

        JButton color1 = new JButton("1st Color");
        JButton color2 = new JButton("2nd Color");
        JLabel l2 = new JLabel("Line Width");
        JLabel l3 = new JLabel("Dash Length");

        setLayout(new FlowLayout());
        add(undo);
        add(clear);
        add(l1);
        add(shapes);
        add(filled);
        add(UseGradient);
        add(color1);
        add(color2);
        add(l2);
        add(width);
        add(l3);
        add(length);
        add(dashed);
        canvas.setBackground(Color.WHITE);
        canvas.setPreferredSize(new Dimension(600, 450));
        add(canvas);
        add(location);

        //button action handlers
        color1.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        c1 = JColorChooser.showDialog(GraphicsFrame.this, "Choose a color", c1);

                        if (c1 == null) {
                            c1 = Color.LIGHT_GRAY;
                            p1 = c1;
                        }
                        repaint();
                    }

                }
        );

        color2.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        c2 = JColorChooser.showDialog(GraphicsFrame.this, "Choose a color", c1);

                        if (c2 == null) {
                            c2 = Color.LIGHT_GRAY;
                        }
                    }

                }
        );

        clear.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        for (int i = 0; i < items.size(); i++) {
                            items.remove(i);
                        }

                        canvas.clear();

                    }
                });

        undo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if (!items.isEmpty()) {
                            int remove = items.size() - 1;
                            items.remove(remove);
                            canvas.repaint();
                        }
                    }
                });
        

    }

    class DrawPanel extends JPanel {

        public DrawPanel() {

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    x[0] = e.getX();
                    y[0] = e.getY();

                    erase = 0;

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    x[1] = e.getX();
                    y[1] = e.getY();
                    addItem();
                    if (erase == 1 && !items.isEmpty()) {
                        int remove = items.size() - 1;
                        items.remove(remove);
                    }
                    erase = 0;
                }

            });

            addMouseMotionListener(
                    new MouseMotionAdapter() {

                        @Override
                        public void mouseDragged(MouseEvent e) {

                            String loc = String.format("( %d , %d )", e.getX(), e.getY());
                            location.setText(loc);
                            x[1] = e.getX();
                            y[1] = e.getY();
                            if (erase == 1 && !items.isEmpty()) {
                                int remove = items.size() - 1;
                                items.remove(remove);
                            }
                            erase = 1;
                            addItem();

                        }

                        @Override

                        public void mouseMoved(MouseEvent e) {
                            String loc = String.format("( %d , %d )", e.getX(), e.getY());
                            location.setText(loc);
                        }

                    });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) {
                image = createImage(getSize().width, getSize().height);

                g2d = (Graphics2D) image.getGraphics();

            }
            clear();
            if (!items.isEmpty()) {
                for (ItemstoDraw i : items) {
                    i.paint(g2d, x, y);
                }
            }
            g.drawImage(image, 0, 0, null);

        }

        public void addItem() {

            
            float w = Float.parseFloat(width.getText());
            float l = Float.parseFloat(length.getText());
            BasicStroke stroke = new BasicStroke(w);
            if (w == 0) {
                w = 1;
            }
            if (l == 0) {
                l = 1;
            }
            

            if (dashed.isSelected()) {
                float[] dashes = {10};

                stroke = new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, l, dashes, 0);
            }

            p1 = c1;

            if (UseGradient.isSelected()) {
                p1 = new GradientPaint(x[0], y[0], c1, x[1], y[1], c2);
            }

            switch (shapes.getSelectedItem().toString()) {
                case "Rectangle":
                    if (filled.isSelected()) {
                        items.add(new ItemstoDraw("Rectangle", p1, true, stroke));

                    } else {

                        items.add(new ItemstoDraw("Rectangle", p1, false, stroke));
                    }
                    break;
                case "Oval":
                    if (filled.isSelected()) {
                        items.add(new ItemstoDraw("Oval", p1, true, stroke));
                    } else {

                        items.add(new ItemstoDraw("Oval", p1, false, stroke));
                    }
                    break;
                case "Line":

                    items.add(new ItemstoDraw("Line", p1, true, stroke));

            }
            repaint();
        }

        public void clear() {

            g2d.setPaint(Color.white);
            g2d.fillRect(0, 0, getSize().width, getSize().height);

            repaint();
        }

    }
}
