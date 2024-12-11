import java.util.*;

class hexCompilerHigh {

    public static class HexFunc{
        int pos;
        String func;
    }


    public static class HexMemory{
        private HashMap<String, HexFunc> functions = new HashMap<>();

        private HashMap<String, Integer> variables = new HashMap<>();

        private Stack<Integer> stack = new Stack<>();

        public HexMemory(){
            this.stack.push(1);
        }

        public void addVar(String varName){
            int newPos = stack.pop();
            if(stack.isEmpty()){
                stack.push(newPos + 1);
            }
            variables.put(varName, newPos);
        }

        public void removeVar(String varName){
            stack.push(variables.get(varName));
            variables.remove(varName);
        }


        public void addFunc(String function, String name){
            int newPos = stack.pop();
            if(stack.isEmpty()){
                stack.push(newPos + 1);
            }
            HexFunc func = new HexFunc();
            func.pos = newPos;
            func.func = function;
            functions.put(name, func);
        }

        public void removeFunc(String name){
            HexFunc func = functions.get(name);
            stack.push(func.pos);
            functions.remove(name);
        }
    }


    public static void main(String... Args){

    }



}
