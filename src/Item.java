import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

enum State {
    ONGOING,
    REJECTED,
    INVITED
}

public class Item {
    private String[] strValues;
    private State state;

    public Item(int numColumns) {
        strValues = new String[numColumns-1];
        state = State.ONGOING;
    }

    public Item(String[] strArr) {
        strValues = strArr;
        state = State.ONGOING;
    }

    public int length(){
        return strValues.length + 1;
    }

    public Date getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        formatter.setLenient(false);
        Date ld = new Date();
        try {
            ld = formatter.parse(strValues[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ld;
    }

    public String getCompany() {
        return strValues[1];
    }
    
    public String getPosition() {
        return strValues[2];
    }

    public String getState(){
        return state.toString();
    }

    public String[] getColumnsStr() {
        return strValues;
    }

    public Object[] getColumnsAll() {
        Object[] cols = new Object[strValues.length + 1];
        for (int i = 0; i < strValues.length; i++) {
            cols[i] = strValues[i];
        }
        cols[strValues.length] = state;
        return cols;
    }

    public Object getValueAt(int columnIndex) {
        if (columnIndex == strValues.length) {
            return state;
        }
        return strValues[columnIndex];
    }

    public void setValueAt(int columnIndex, Object val) {
        if (columnIndex == strValues.length) {
            state = asState(val);
        } else {
            strValues[columnIndex] = (String) val;
        }
    }

    public State asState(Object s) {
        String str = s.toString();
        switch (str) {
            case "ONGOING":
                return State.ONGOING;
            case "REJECTED":
                return State.REJECTED;
            case "INVITED":
                return State.INVITED;
            default:
                return State.ONGOING;
        }
    }

    public void print() {
        String out = "";
        out += "| ";
        for (int i = 0; i < strValues.length; i++) {
             out += strValues[i] + " | ";
        }
        out += state + " |";

        int lengthBarrier = out.length();
        String barrier = "";
        for(int i = 0; i < lengthBarrier; i++) {
            barrier += "-";
        }

        System.out.println(barrier);
        System.out.println(out);
        System.out.println(barrier);
    }
}
