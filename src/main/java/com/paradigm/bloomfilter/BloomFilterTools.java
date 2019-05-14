package com.paradigm.bloomfilter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import com.google.common.hash.BloomFilter;
import com.paradigm.log.CommonLogger;

public class BloomFilterTools {
    // group size limit int
    public static int getActionGroupIndex(String action, int groupSize) {
        CRC32 crc = new CRC32();
        crc.update(action.getBytes());
        return (int)(crc.getValue() % groupSize);
    }
    
    public static void saveEachBloolFiterToFile(String fileName, BloomFilter<String> bloomFilter) {
        File file = new File(fileName + "_backup");
        if (file.exists()) {
            file.delete();
        }
        BloomFilter<String> temp = bloomFilter.copy();
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            temp.writeTo(outputStream);
            File destFile = new File(fileName);
            file.renameTo(destFile);
            
            CommonLogger.info("save bloomfilter {} to file ", fileName);
        } catch (Exception ex) {
            CommonLogger.error("save to bloomfilter file error, ex:", ex);
            // alter 报警、Mail等
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    CommonLogger.error("output stream error, e:", e);
                }
            }
        }
    }
}
