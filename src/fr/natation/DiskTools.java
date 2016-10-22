package fr.natation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class DiskTools {

    public static void fileCopy(File infile, File outfile) throws IOException {
        //Générer une exception si le fichier destination existe déjà
        if (outfile.exists()) {
            throw new IOException("Destination file already exists");
        }

        @SuppressWarnings("resource")
        FileChannel inFileChannel = new FileInputStream(infile).getChannel();
        @SuppressWarnings("resource")
        FileChannel outFileChannel = new FileOutputStream(outfile).getChannel();
        try {
            // magic number for Windows, 64Mb - 32Kb)
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inFileChannel.size();
            long offset = 0;
            while (offset < size) {
                offset += inFileChannel.transferTo(offset, maxCount, outFileChannel);
            }
        } finally {
            if (inFileChannel != null) {
                inFileChannel.close();
            }
            if (outFileChannel != null) {
                outFileChannel.close();
            }
        }
    }

    public static void threadCopy(Path src, Path dest) throws IOException {
        long start = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Files.copy(src, dest);
                } catch (IOException e) {
                }
            }
        }).start();
        ;

        long stop = System.currentTimeMillis();
        System.err.println((stop - start));
    }

}