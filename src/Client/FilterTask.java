package Client;

import Server.Task;
import Server.TaskResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class FilterTask implements Task {
    private final int id;
    private final byte [] serializedImage;

    public static final int TOP_LEFT =1;
    public static final int TOP_RIGHT =2;
    public static final int BOTTOM_LEFT =3;
    public static final int BOTTOM_RIGHT =4;
    public FilterTask(int id, byte[] serializedImage) {
        this.id = id;
        this.serializedImage = serializedImage;
    }


    public static BufferedImage convertByteArrayToBufferedImage(byte[] imageBytes) {
        BufferedImage bufferedImage = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            bufferedImage = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }



    public static byte[] convertBufferedImageToByteArray(BufferedImage image, String format) {
        byte[] imageBytes = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, format, byteArrayOutputStream);
            imageBytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;
    }
    static byte[] splitImage(byte[] serializedImage, int square) {
        BufferedImage im =convertByteArrayToBufferedImage(serializedImage);
        BufferedImage image = null;

        if (im == null) {
            return null;
        }
        if(square ==1)
            image = im.getSubimage(0, 0, im.getWidth() / 2, im.getHeight() / 2);
        else if(square ==2)
            image = im.getSubimage(im.getWidth() / 2, 0, im.getWidth() / 2, im.getHeight() / 2);
        else if(square ==3)
            image = im.getSubimage(0, im.getHeight() / 2, im.getWidth() / 2, im.getHeight() / 2);
        else if(square ==4)
            image = im.getSubimage(im.getWidth() / 2, im.getHeight() / 2, im.getWidth() / 2, im.getHeight() / 2);

        return convertBufferedImageToByteArray(image,"png");
    }


    static byte[] mergeImages(byte[] topLeftSerialized, byte[]  topRightSerialized, byte[]  bottomLeftSerialized, byte[]  bottomRightSerialized) throws IOException {
        BufferedImage topLeft =convertByteArrayToBufferedImage(topLeftSerialized);
        BufferedImage topRight =convertByteArrayToBufferedImage(topRightSerialized);
        BufferedImage bottomLeft =convertByteArrayToBufferedImage(bottomLeftSerialized);
        BufferedImage bottomRight =convertByteArrayToBufferedImage(bottomRightSerialized);


        int width = topLeft.getWidth() + topRight.getWidth();
        int height = topLeft.getHeight() + topRight.getHeight();
        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = mergedImage.getGraphics();
        graphics.drawImage(topLeft, 1, 1, null);
        graphics.drawImage(topRight, width / 2, 1, null);
        graphics.drawImage(bottomLeft, 1, height / 2, null);
        graphics.drawImage(bottomRight, width / 2, height / 2, null);



        return convertBufferedImageToByteArray(mergedImage,"png");
    }

    static void sendToSlave(byte [] serializedImage, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(serializedImage.length);
        dataOutputStream.write(serializedImage);
        outputStream.flush();

    }

    static byte [] recieveFromSlave(Socket socket) throws IOException {
        InputStream inputStream=socket.getInputStream();
        DataInputStream dataInputStream=new DataInputStream(inputStream);
        int fileLength = dataInputStream.readInt();
        byte [] filteredImageSerialized=new byte[fileLength];
        dataInputStream.readFully(filteredImageSerialized,0,fileLength);
        //socket.close();
        inputStream.close();
        dataInputStream.close();
        return filteredImageSerialized;
    }


    @Override
    public TaskResult execute() {
        try {
            Socket socketTopRight = new Socket("localhost",3030);
            Socket socketTopLeft = new Socket("localhost",3040);
            Socket socketBottomLeft = new Socket("localhost",3050);
            Socket socketBottomRight = new Socket("localhost",3060);

            byte [] serializedImageTopLeft=splitImage(serializedImage,TOP_LEFT);
            byte [] serializedImageTopRight=splitImage(serializedImage,TOP_RIGHT);
            byte [] serializedImageBottomLeft=splitImage(serializedImage,BOTTOM_LEFT);
            byte [] serializedImageBottomRight=splitImage(serializedImage,BOTTOM_RIGHT);

            sendToSlave(serializedImageTopLeft,socketTopLeft);
            sendToSlave(serializedImageTopRight,socketTopRight);
            sendToSlave(serializedImageBottomLeft,socketBottomLeft);
            sendToSlave(serializedImageBottomRight,socketBottomRight);

            byte [] filteredTopLeftImageSerialized=recieveFromSlave(socketTopLeft);
            byte [] filteredTopRightImageSerialized=recieveFromSlave(socketTopRight);
            byte [] filteredBottomLeftImageSerialized=recieveFromSlave(socketBottomLeft);
            byte [] filteredBottomRightImageSerialized=recieveFromSlave(socketBottomRight);

            byte [] filteredImageSerialized=mergeImages(filteredTopLeftImageSerialized,
                                                        filteredTopRightImageSerialized,
                                                        filteredBottomLeftImageSerialized,
                                                        filteredBottomRightImageSerialized);



            return new TaskResult(id,filteredImageSerialized);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void run() {

    }
}
