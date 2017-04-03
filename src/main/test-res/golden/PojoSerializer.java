package golden;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vi34.raw.Pojo;

import java.io.IOException;

public class PojoSerializer extends StdSerializer<Pojo> {

    public PojoSerializer() {
        this(null);
    }

    protected PojoSerializer(Class<Pojo> t) {
        super(t);
    }

    @Override
    public void serialize(Pojo value, JsonGenerator gen, SerializerProvider provider) throws IOException {

    }
}
