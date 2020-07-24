package IGSquarer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Configuration
{
	private Map<String, String> data;
	private File				file;
	
	public Configuration( File file )
	{
		this.data = new Hashtable<String, String>( );
		this.file = file;
	}
	
	public String getValue( String key )
	{
		return data.get(key);
	}
	
	public void setValue( String key, String value )
	{
		data.put(key, value);
	}
	
	public void save( )
	{
		BufferedWriter bw = null;
		
		try
		{
			bw = new BufferedWriter( new FileWriter(file) );
			
			Set<String> keys = data.keySet();
			String line;
			
			for (String key : keys)
			{
				line = key + "=" + data.get(key) + "\n";
				bw.write( line );
			}
		}
		catch (IOException e)
		{
		}
		finally
		{
			try
			{
				if (bw != null)
					bw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void load( )
	{
		BufferedReader br = null;
		
		try
		{
			br = new BufferedReader( new FileReader(file) );
			
			String line;
			String[] values;
			
			while ((line = br.readLine()) != null)
			{
				line.replace("\n", "").replace("\r", "");
				values = line.split("=");
				data.put(values[0], values[1]);
			}
		}
		catch (IOException e)
		{
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
