package y2019.day9;

import y2019.intcode.IntCode;

public class Main9 {

    public static void main(String[] args) {
        String filePath = "y2019/day9/input.txt";
        long[] arr1 = new long[] {109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99};
        long[] arr2 = new long[] {1102,34915192,34915192,7,4,7,99,0};
        long[] arr3 = new long[] {104,1125899906842624L,99};
//        IntCode intCode = new IntCode().initialiseFromArr(arr2);
        IntCode intCode = new IntCode().initialiseFromPath(filePath);
        intCode.iterate();
    }

}
