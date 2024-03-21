package Slave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GreenFilter {
    public static byte[] serializedImage;
    public static int port;


    public static void setGreenFilter() throws IOException {

        InputStream is = new ByteArrayInputStream(serializedImage);
        BufferedImage img = ImageIO.read(is);


        for(int i=0 ; i < img.getHeight() ; i++){
            for(int j=0 ; j < img.getWidth() ; j++){

                Color c = new Color(img.getRGB(j,i));

                int green = 255 - c.getGreen();

                Color newColor = new Color(0,green,0);
                img.setRGB(j,i,newColor.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        serializedImage = baos.toByteArray();
    }


    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);

        } else {

            throw new RuntimeException();
        }
        ServerSocket serverScoket =new ServerSocket(port);//port =3030,3040,3050,3060
        System.out.println("GreenFilter salve is ready on port "+port+" ...");
        while(true){
            Socket socket=serverScoket.accept();
            InputStream inputStream=socket.getInputStream();
            DataInputStream dataInputStream=new DataInputStream(inputStream);
            //TODO add while true here

            int fileLength = dataInputStream.readInt();
            if(fileLength>0){
                serializedImage =new byte[fileLength];
                dataInputStream.readFully(serializedImage,0,fileLength);
            }else{
                System.out.println("file's empty!");
            }
            //output();
            setGreenFilter();
            //sending back the file

            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(serializedImage.length);

            dataOutputStream.write(serializedImage);
            System.out.println("slave has finished!");
        }

        //TODO : end of while true loop
       // inputStream.close();
        //outputStream.close();

    }
}
