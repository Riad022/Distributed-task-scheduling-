package Slave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConvolutionFilter {
    public static byte[] serializedImage;
    public static int port;
    public static float[] kernel = new float[9];

    public static void Convole() throws IOException {
        InputStream is = new ByteArrayInputStream(serializedImage);
        BufferedImage inputimage = ImageIO.read(is);
        Kernel kernel1 = new Kernel(3, 3, kernel);
        ConvolveOp convolution = new ConvolveOp(kernel1);
        BufferedImage outputimage = convolution.filter(inputimage, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputimage, "png", baos);
        serializedImage = baos.toByteArray();
    }



    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);

        } else {
            throw new RuntimeException();
        }
        ServerSocket serverScoket =new ServerSocket(port);//port =4030,4040,4050,4060
        System.out.println("ConvolutionFilter salve is ready on port "+port+" ...");
        while(true){
            Socket socket=serverScoket.accept();
            InputStream inputStream=socket.getInputStream();
            DataInputStream dataInputStream=new DataInputStream(inputStream);
            //TODO add while true here

            for (int i = 0; i < 9; i++) {
                float value=dataInputStream.readFloat();
                kernel[i] = value;
            }


            int fileLength = dataInputStream.readInt();
            if(fileLength>0){
                serializedImage =new byte[fileLength];
                dataInputStream.readFully(serializedImage,0,fileLength);
            }else{
                System.out.println("file's empty!");
            }
            //output();
            Convole();
            //sending back the file

            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(serializedImage.length);

            dataOutputStream.write(serializedImage);
            System.out.println("Slave has finished!");
        }

        //TODO : end of while true loop
        //inputStream.close();
        //outputStream.close();

    }
}
