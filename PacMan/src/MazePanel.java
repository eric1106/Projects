//*** MazePanel
//*** Forouraghi

import java.awt.*;
import javax.swing.*;

//*********************************************************
class MazePanel extends JPanel
{

   JPanel[][] wall;
   ImageIcon  icon;
   JLabel     label;

   //******************************************************
   //*** main constructor
   //******************************************************
   MazePanel(int[][] mazePlan, boolean[][] visited)
   {
   setLayout(new GridLayout(mazePlan.length, mazePlan[0].length));
   wall = new JPanel[mazePlan.length][mazePlan[0].length];
   visited = new boolean[mazePlan.length][mazePlan[0].length];


      //*** draw the maze
      for (int i=0; i<mazePlan.length; i++)
        for (int j=0; j<mazePlan[0].length;j++) {
           //*** initially, mark each state as unvisited
           visited[i][j] = false;

           wall[i][j] = new JPanel();

           //*** mark power pellets
           if (mazePlan[i][j] == 0)
             setupChar(i, j, "pellet.gif");

           //*** color blue marks walls
           else if (mazePlan[i][j] == 1) {
               wall[i][j].setBackground(Color.blue);
               wall[i][j].setBorder(BorderFactory.createLineBorder (Color.blue, 1));
           }

           //*** mark the striped enemy
           else if (mazePlan[i][j] == 2)
             setupChar(i, j, "stripes.gif");

           //*** mark the pirate
           else if (mazePlan[i][j] == 3)
             setupChar(i, j, "pirate.gif");

        add(wall[i][j]);
        }
   }



   //******************************************************
   //*** place an image in a cell
   //******************************************************
   void setupChar(int i, int j, String charac)
   {
       icon = new ImageIcon(charac);
       label = new JLabel(icon);

       wall[i][j].setBackground(Color.black);
       wall[i][j].removeAll();
       wall[i][j].add(label);
       wall[i][j].validate();
   }



   //******************************************************
   //*** erase image from a cell by replacing it with blank
   //******************************************************
   void removeChar(int i, int j)
   {
       icon = new ImageIcon("block.gif");
       label = new JLabel(icon);
       wall[i][j].removeAll();
       wall[i][j].add(label);

       wall[i][j].setVisible(true);
       wall[i][j].validate();
   }

} // MazePanel
