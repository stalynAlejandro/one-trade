package com.pagonxt.onetradefinance.integrations.model.document;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Model class with methods that allow us to perform certain operations with documents.
 * @author -
 * @version jdk-11.0.13
 * @see Document
 * @since jdk-11.0.13
 */
public class DocumentMultipartFile implements MultipartFile {
    private final byte[] fileContent;
    private final Document document;

    /**
     * constructor method
     * @param document : a Document object
     */
    public DocumentMultipartFile(Document document) {
        this.document = document;
        this.fileContent = Base64.getDecoder().decode(document.getData().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * class method
     * @return a string with file name
     */
    @Override
    public String getName() {
        return this.document.getFilename();
    }

    /**
     * class method
     * @return a string with the original file name
     */
    @Override
    public String getOriginalFilename() {
        return this.document.getFilename();
    }

    /**
     * class method
     * @return a string with the mime type
     */
    @Override
    public String getContentType() {
        return this.document.getMimeType();
    }

    /**
     * class method
     * @return true or false if the document has data
     */
    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    /**
     * class method
     * @return a long value with the document size
     */
    @Override
    public long getSize() {
        return fileContent.length;
    }

    /**
     * this method does the encoding of string into the sequence of bytes and keeps it in an array of bytes.
     * @return a sequence of data
     * @throws IOException handles IO exception
     */
    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    /**
     * This method gets the input stream of the subprocess.
     * The stream obtains data piped from the standard output stream
     * of the process represented by this Process object.
     * @return a InputStream object
     * @throws IOException handles IO exceptions
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    /**
     * This method obtains input bytes from a file in a file system
     * @param dest a File object with the file where to transfer data
     * @throws IOException handles IO exceptions
     * @throws IllegalStateException handles illegal state exceptions
     */
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(fileContent);
        }
    }
}
