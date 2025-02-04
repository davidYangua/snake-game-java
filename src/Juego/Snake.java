package Juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class Snake extends JFrame{
    
    private Point snake;
    private Point comida;
    private ImageIcon imgs = new ImageIcon(getClass().getResource("/Imagenes/Snake.png"));
    private ImageSnake img = new ImageSnake();;
    private int direccion = KeyEvent.VK_RIGHT;
    private long frecuencia = 50;
    private List<Point>puntos;
    private boolean gameOver=false;
    private int score = 0;
    private boolean estado=false;
    private JPopupMenu menu = new JPopupMenu();
    private JMenu color = new JMenu();
    private static JMenuItem negro = new JMenuItem();
    private static JMenuItem blanco = new JMenuItem();
    private static JMenuItem verde = new JMenuItem();
    private static JMenuItem gris = new JMenuItem();
    private static Color c = Color.BLACK;
    
    
    public Snake(){
        this.setSize(800, 600);
        this.setTitle("Snake");
        this.setDefaultLookAndFeelDecorated(false);
        JFrame.setDefaultLookAndFeelDecorated(false);
	//setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        
        this.setIconImage(imgs.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        evento();
        iniciar();
        
        Movimiento mv = new Movimiento();
        Thread th = new Thread(mv);
        th.start();
        
        generarMenu();
        eventoAccion();
    }
    
    public void eventoAccion(){
        ActionListener f = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = Color.BLACK;
            }
        };
        negro.addActionListener(f);
        
        ActionListener t = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = Color.WHITE;
            }
        };
        blanco.addActionListener(t);
        
        ActionListener k = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = Color.GREEN;
            }
        };
        verde.addActionListener(k);
        
         ActionListener g = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = Color.GRAY;
            }
        };
        gris.addActionListener(g);
    }
    
    public void generarMenu(){
        color.setText("Cambiar fondo");
        menu.add(color);
        negro.setText("Negro");
        color.add(negro);
        color.addSeparator();
        blanco.setText("Blanco");
        color.add(blanco);
        color.addSeparator();
        verde.setText("Verde");
        color.add(verde);
        color.addSeparator();
        gris.setText("Gris");
        color.add(gris);
    }
    
    public void actualizar(){
        img.repaint();
        puntos.add(0, new Point(snake.x, snake.y));
        puntos.remove(puntos.size()-1);
        
        for(int i=1; i<puntos.size();i++){
            Point pt = puntos.get(i);
            if(snake.x==pt.x && snake.y==pt.y){
                gameOver=true;
            }
        }
        
        if(snake.x>(comida.x-14)&&snake.x<(comida.x+14)&&snake.y>(comida.y-14)&&snake.y<(comida.y+14)){
            puntos.add(0, new Point(snake.x, snake.y));
            score++;
            generarComida();
        }
    }
    
    public void evento(){
        Teclas t = new Teclas();
        this.addKeyListener(t);
    }
    
    public void iniciar(){
        puntos = new ArrayList<>();
        menuDespleable md = new menuDespleable();
        img.addMouseListener(md);
        snake = new Point(378,280);
        puntos.add(snake);
        generarComida();
        
        this.add(img);
    }
    
    public void generarComida(){
        Random r = new Random();
        int a=0; int b=0;
        do {
            a=r.nextInt(760); b=r.nextInt(560);
            comida = new Point(a,b); 
        }while(!(a%14==0 && b%14==0));
    }
    
    public class ImageSnake extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            this.setBackground(c);
            
            g.setColor(Color.BLACK);
            for(int i=0; i<800; i+=14){
                for(int j=0; j<600; j+=14){
                    g.drawRect(i, j, 14, 14);
                }
            }
            
            g.setColor(new Color(0,0,255));
            for(int i=0; i<puntos.size(); i++){
                Point p = puntos.get(i);
                g.fillRect(p.x, p.y, 14, 14);
            }
            
            g.setColor(new Color(255,0,0));
            g.fillOval(comida.x, comida.y, 14, 14);
            
            if(gameOver){
                g.setColor(Color.red);
                g.setFont(new Font("cooper black", Font.PLAIN, 20));
                g.drawString("Game Over ", 300, 260);
                g.drawString("Score: "+score+" pts", 300, 310);
                g.drawString("Press Enter to continue", 300, 360);
            }
        }
    }
    
    public class menuDespleable extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent evt){
            if(evt.isMetaDown()){
                menu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }
    
    public class Teclas extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                estado=true;
                int r = JOptionPane.showConfirmDialog(null, "¿Desea salir al menu principal?", "¿Salir?", JOptionPane.YES_NO_OPTION);
                if(r==JOptionPane.YES_OPTION){
                    Principal pt = new Principal();
                    pt.setVisible(true);
                    dispose();
                }else if(r==JOptionPane.NO_OPTION){
                    estado=false;
                }
            } else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                if(direccion!=KeyEvent.VK_LEFT){
                    direccion = KeyEvent.VK_RIGHT;
                }
            } else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                if(direccion!=KeyEvent.VK_RIGHT){
                    direccion = KeyEvent.VK_LEFT;
                }
            } else if(e.getKeyCode()==KeyEvent.VK_UP){
                if(direccion!=KeyEvent.VK_DOWN){
                    direccion = KeyEvent.VK_UP;
                }
            } else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                if(direccion!=KeyEvent.VK_UP){
                    direccion = KeyEvent.VK_DOWN;
                }
            }else if(e.getKeyCode()==KeyEvent.VK_ENTER){
                if(gameOver){
                    Score.agregarScore(score);
                    gameOver=false;
                    Principal pc = new Principal();
                    pc.setVisible(true);
                    dispose();
                }
            }
        }
    }
    
    public class Movimiento extends Thread{
        long tiempo = 0;
        @Override
        public void run(){
            while(true){
                if((System.currentTimeMillis()-tiempo)>frecuencia){
                    if(!(gameOver||estado)){
                        if (direccion == KeyEvent.VK_RIGHT) {
                            snake.x += 14;
                            if (snake.x > 780) {
                                snake.x = 0;
                            }
                        } else if (direccion == KeyEvent.VK_LEFT) {
                            snake.x -= 14;
                            if (snake.x < 0) {
                                snake.x = 780;
                            }
                        } else if (direccion == KeyEvent.VK_UP) {
                            snake.y -= 14;
                            if (snake.y < 0) {
                                snake.y = 550;
                            }
                        } else if (direccion == KeyEvent.VK_DOWN) {
                            snake.y += 14;
                            if (snake.y > 550) {
                                snake.y = 0;
                            }
                        }
                        tiempo = System.currentTimeMillis();
                        actualizar();
                    }
                }
            }
        }
    }
}

