import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vi34.annotations.Json;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by vi34 on 13/08/16.
 */

@Json
public class Main {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Pojo pojo = new Pojo(1, "test", Arrays.asList(3, 4 ,5 ,1));
        Pojo pojo2 = new Pojo(2, "test2", Arrays.asList(3, 4 ,5 ,1));
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.writeValue(System.out, pojo);
        mapper.writeValue(System.out, pojo2);

    }
}
