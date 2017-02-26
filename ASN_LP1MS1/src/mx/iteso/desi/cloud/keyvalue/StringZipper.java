/* 
 * Copyright 2017 Mario Contreras <marioc@nazul.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.iteso.desi.cloud.keyvalue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Couple of String gzipping utilities.
 *
 * @author Scott McMaster and Ávaro Parres
 *
 */
public class StringZipper {

    /**
     * Gzip the input string into a byte[].
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] zipStringToBytes(String input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedOutputStream bufos = new BufferedOutputStream(new GZIPOutputStream(bos));
        bufos.write(input.getBytes());
        bufos.close();
        byte[] retval = bos.toByteArray();
        bos.close();
        return retval;
    }

    /**
     * Unzip a string out of the given gzipped byte array.
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String unzipStringFromBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        BufferedInputStream bufis = new BufferedInputStream(new GZIPInputStream(bis));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = bufis.read(buf)) > 0) {
            bos.write(buf, 0, len);
        }
        String retval = bos.toString();
        bis.close();
        bufis.close();
        bos.close();
        return retval;
    }

    /**
     * Static class.
     *
     */
    private StringZipper() {
    }
}

// EOF
