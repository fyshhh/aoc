package y2019.day5;

import y2019.intcode.IntCode;

public class Main5 {

    public static void main(String[] args) {
//        IntCode intCode = new IntCode().initialiseFromArr(new int[] {
//                3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
//                1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
//                999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99});
        IntCode intCode = new IntCode().initialiseFromPath("y2019/day5/input.txt");
        intCode.process();
    }

}
