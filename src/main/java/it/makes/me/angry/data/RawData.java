package it.makes.me.angry.data;



public class RawData {
    public final String fileContent;

    public RawData(final String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RawData rawData = (RawData) o;

        return fileContent.equals(rawData.fileContent);

    }

    @Override
    public int hashCode() {
        return fileContent.hashCode();
    }
}
