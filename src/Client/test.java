package Client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;

public class test {


    public static int generateId() {
        Random rand = new Random();
        int id = rand.nextInt(1000);
        return id;
    }

    public static void main(String[] args) throws IOException {
        for(int i=0;i<5;i++)
            System.out.println(generateId());

    }
}
