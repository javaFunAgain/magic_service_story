package it.makes.me.angry.producer;


public class RawData {
    public final String fileContent;

    public RawData(final String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public boolean equals(Object o) {
        return fileContent.equals(((RawData)o).fileContent);//TODO - does not conform to Object.equals  - null problem
    }

    @Override
    public int hashCode() {
        return fileContent.hashCode();
    }



}
