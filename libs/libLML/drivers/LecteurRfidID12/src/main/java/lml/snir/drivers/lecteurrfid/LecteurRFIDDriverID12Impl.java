package lml.snir.drivers.lecteurrfid;

// lml.snir.drivers.lecteurrfid.LecteurRFIDDriverID12Impl

import java.util.ArrayList;
import java.util.List;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import lml.snir.drivers.LMLDriverObservable;
/**
 *
 * @author fanou
 */
public class LecteurRFIDDriverID12Impl extends LMLDriverObservable {

    private final SerialPort serialPort;

    public LecteurRFIDDriverID12Impl(String portName) {
        serialPort = new SerialPort(portName);
    }

    @Override
    public void open() throws Exception {
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            String name = ex.getPortName();
            String msg = ex.getExceptionType();
            throw new Exception(name + " " + msg);
        }
    }

    @Override
    public void close() throws Exception {
        try {
            this.serialPort.closePort();
        } catch (SerialPortException ex) {
            //System.err.println("LecteurRFIDDriverID12Impl.close() : " + ex.getMessage());
        }
    }

    @Override
    public void write(byte[] bytes) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] read() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class PortReader implements SerialPortEventListener {

        private final List<Byte> buffer = new ArrayList<>();
        private int length;
        private final byte STX = 0x02;
        private final byte ETX = 0x03;
        private final byte LF = 0x0A;
        private final byte CR = 0x0D;
        private boolean startOfFrame = false;
        private int cpt = 0;
        private StringBuilder str;
        private int checkSum = 0;

        // Frame = STX | Data (10 ASCII Bytes) | CheckSum (XOR 2 Bytes) | CR | LF | ETX  => 16 bytes en tout
        @Override
        public void serialEvent(SerialPortEvent event) {
            int value = event.getEventValue();
            if (value > 0) {
                try {
                    Thread.sleep(250);
                    byte[] receivedData = serialPort.readBytes();
                    if (receivedData.length != 16) {
                        // pas tout de reçu re read pour compléter
                    }

                    int index = 0;
                    this.cpt = 0;
                    this.str = new StringBuilder();
                    this.checkSum = 0;
                    while (index < receivedData.length & !(this.startOfFrame = (receivedData[index++] == STX)));

                    this.cpt++;
                    boolean msb = true;
                    int octet = 0;
                    while (cpt++ < 11 & index < receivedData.length) {
                        char ch = (char) receivedData[index++];
                        this.str.append(String.valueOf(ch));
                        //this.checkSum ^= this.toInt(ch);

                        if (msb) {
                            octet = this.toInt(ch) * 16;
                        } else {
                            octet += this.toInt(ch);
                            this.checkSum ^= octet;
                        }
                        msb = !msb;
                    }
                    // recup crc
                    int crcReceived = this.toInt((char) receivedData[index++]) * 16 + this.toInt((char) receivedData[index++]);

                    // check CR / LF / ETX
                    if (crcReceived == this.checkSum) {
                        setChanged();
                        String content = this.str.toString();
                        String strId = content.substring(4);
                        int id = Integer.parseInt(strId, 16);
                        String tmp = String.valueOf(id);
                        strId = "";
                        for (int i=0; i < 10 - tmp.length(); i++) {
                            strId += "0";
                        }
                        strId += tmp;
                        notifyObservers(strId);
                    }

                } catch (Exception ex) {

                }
            }
        }

        private int toInt(char ch) {
            int result = 0;
            if (Character.isDigit(ch)) {
                result = ch - 0x30;
            } else {
                if (Character.isLetter(ch)) {
                    result = 10 + (ch - 'A');
                }
            }

            return result;
        }

    }
}
