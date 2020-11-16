
package dk.sdu.mmmi.rd1.edgedetect;

import dk.sdu.mmmi.rd1.robotcomm.RobotClient;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class ImportImage
{
    //We Start by making borders to the image and make a line saparator when the line ends
    public String drawDelimiter(int x, int y, int width, int height) 
    {   
        if (x != width - 1) {
            return "";
        } else {
            if (y != height - 1) {
                return System.lineSeparator();
               
                
            } else {
                return "End";
            }
        }
        
    }
    
    //1 = Black , 0 = white.
     public int binaryTrunc (int a)
        {
         if(a <= 127)
             return 1;
         else
             return 0;
        }   
     
     //In Picture Class We have BefferedImage that cane Resize the 
     
    private BufferedImage getScaledImage(BufferedImage src, int w, int h){
        int original_width = src.getWidth();
        int original_height = src.getHeight();
        int bound_width = w;
        int bound_height = h;
        int new_width = original_width;
        int new_height = original_height;

       
        if (original_width > bound_width) {
           
            new_width = bound_width;
         
            new_height = (new_width * original_height) / original_width;
        }

        if (new_height > bound_height) {
          
            new_height = bound_height;
          
            new_width = (new_height * original_width) / original_height;
        }

        BufferedImage resizedImg = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.drawImage(src, 0, 0, new_width, new_height, null);
        g2.dispose();
        return resizedImg;
    }
    
    public static void main(String[] args)
    {
        File input = new File("D:\\funny.png");
       
        ImportImage importImage = new ImportImage();
        
        
        try
        {
            BufferedImage image = ImageIO.read(input);
            BufferedImage resized = importImage.getScaledImage(image, 70, 70);
            File output = new File("D:\\TegnerobotResized.jpg");
            ImageIO.write(resized, "jpg", output);
        }
        
        catch(IllegalArgumentException e)
        {
            System.out.println("Illegal Argument Exception - Input is null. Choose a different file");    
        } 
        catch(IOException ex)
        {
            System.out.println("Wrong input file or output destination");
        }         
              
       
        EdgeDetector edge = new EdgeDetector("D:\\TegnerobotResized.jpg");
        
        



        BufferedImage after = edge.getBufferedImage();                     
        
        
        
        
        
        int[][] magnitudeImg =edge.getMagnitudeArray();  
        
        
               
        int height = after.getHeight();
        int width = after.getWidth();       
              
        
        String outputString = " ";
        
        
        for (int y = 0; y < height; y++) 
        {
            
            
            for (int x = 0; x < width; x++) 
            {
                //Outputstring bruges, når vi vil lave en komplet string af hele billedet.
                
                outputString += importImage.binaryTrunc(magnitudeImg[x][y]) + importImage.drawDelimiter(x, y, width, height);
                
                
                        
            }
        }
        System.out.println(outputString);
        
                
        //Connecting to the PLC and sending the string.
     
        RobotClient client = new RobotClient("127.0.0.1", 12345);
        
        client.connect();
         
        client.write(outputString);
        
        
        client.disconnect();
    }

}
