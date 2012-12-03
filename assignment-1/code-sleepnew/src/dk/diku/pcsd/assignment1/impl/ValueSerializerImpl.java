package dk.diku.pcsd.assignment1.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import dk.diku.pcsd.keyvaluebase.interfaces.ValueSerializer;

public class ValueSerializerImpl implements ValueSerializer<ValueListImpl> {

	@Override
	public ValueListImpl fromByteArray(byte[] array) throws IOException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(array);
		ObjectInputStream is = new ObjectInputStream(byteStream);
		
		Object result = null;
		try {
			result = is.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (result instanceof ValueListImpl){
			return (ValueListImpl) result;
		}else{
			return null;
		}
	}

	@Override
	public byte[] toByteArray(ValueListImpl v) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(byteStream);
		os.writeObject(v);
		
		os.close();
		
		byte[] result = byteStream.toByteArray();
		
		return result;
	}

}
