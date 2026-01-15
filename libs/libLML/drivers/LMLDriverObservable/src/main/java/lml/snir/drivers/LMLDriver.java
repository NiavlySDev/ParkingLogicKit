package lml.snir.drivers;

/**
 *
 * @author fanou
 */
public interface LMLDriver {
    public void open() throws Exception;
    public void close() throws Exception;
    public void write(byte[] datas) throws Exception;
    public byte[] read() throws Exception;
}
