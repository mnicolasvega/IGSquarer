package IGSquarer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class Processor implements Runnable
{
	private final int		WATERMARK_SEPARATION = 10;
	
	
	
	private String			src,
							dst;
	private int				size,
							sizeWM,
							wmPos;
	private Color			color;
	private GUI				gui;
	private boolean			run;
	private BufferedImage	wm,
							fondo;
	
	
	
	public Processor( String src, String dst, int size, Color color, BufferedImage fondo, BufferedImage wm, int sizeWM, int wmPos, GUI gui )
	{
		this.src	= src;
		this.dst	= dst;
		this.size	= size;
		this.color	= color;
		this.gui	= gui;
		this.fondo	= fondo;
		this.sizeWM = sizeWM;
		this.wmPos	= wmPos;
		this.wm		= wm;
	}
	
	
	public void run()
	{
		run	= true;
		
		int			failedCount = 0,
					successCount = 0;
		String		name,
					format;
		List<File>	archivos = listFiles( new File(src) );

		gui.updateStatus( String.format("procesado 0/%d (fallidos: 0)", archivos.size()) );
		
		for (File f : archivos)
		{
			if (!run)
			{
				gui.updateStatus( String.format("detenido: %d/%d (fallidos: %d)", (failedCount + successCount), archivos.size(), failedCount) );
				break;
			}
			
			try
			{
				System.out.println( f );
				BufferedImage img = ImageIO.read( f );
				img = fitImageIntoSquare( img, size, color );
				
				name	= f.getName();
				format	= getExtension( f.getName() );
				
				try
				{
					ImageIO.write(img, format, new File( dst + name ) );
					successCount ++;
				}
				catch (IOException e)
				{
					e.printStackTrace();
					failedCount ++;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				failedCount ++;
			}
			
			gui.updateStatus( String.format("procesado %d/%d (fallidos: %d)", (failedCount + successCount), archivos.size(), failedCount) );
		}

		if (run)
			gui.finishedParsing( true );
	}
	
	
	public void stop( )
	{
		run = false;
	}
	
	private List<File> listFiles( File folder )
	{
		List<File> l = new LinkedList<File>( );
		
		String[] files = folder.list();
		String extension;
		File f;
		
		for (String sz : files)
		{
			f = new File(src + sz);
			if (!f.isDirectory())
				l.add(f);
		}
		
		return l;
	}
	
	private String getExtension( String file )
	{
		String res = "";
		
		int p = file.lastIndexOf(".");
		if (p > -1)
			res = file.substring(p + 1);
		
		return res;
	}
	
	private String onlyFileName( String file )
	{
		String res = "";
		
		int p = file.lastIndexOf("/");
		if (p > -1)
			res = file.substring(p + 1);
		
		return res;
	}
	
	
	/**
	 * Recibe una imagen y la reajusta para que encaje en el centro de un cuadrado de lados 'size' con color de fondo 'color'.
	 */
	private BufferedImage fitImageIntoSquare( BufferedImage img, int size, Color color )
	{
		BufferedImage	res = new BufferedImage( size, size, img.getType() ),	// Imagen a modificar.
						tmp = resizeImageToMax( img, size );					// Imagen original luego de adaptarse al nuevo tamaño.
	
		// Obtener las coordenadas para pintar la imagen en el centro.
		int x,
			y,
			w = tmp.getWidth(),
			h = tmp.getHeight(),
			xStart = 0,
			yStart = 0;
		
		if (h >= w)
			xStart = ((size - w) / 2) - 1;
		else
			yStart = ((size - h) / 2) - 1;
				
		// Preparar el dibujo
		Graphics g = res.getGraphics();
		
		// Color sólido de fondo siempre
		g.setColor( color );
		g.fillRect(0, 0, size, size);
		
		// Si hay imagen de fondo, dibujarla sobre el fondo (se contempla transparencia)
		if (fondo != null)
			g.drawImage(fondo, 0, 0, size, size, null);
		
		// Dibujar la imagen correctamente centrada
		g.drawImage(tmp, xStart, yStart, w, h, null);
		
		// Dibujar la marca de agua
		switch (wmPos)
		{
			// Up-left
			case 1:
			{
				g.drawImage(
					wm,
					WATERMARK_SEPARATION,
					WATERMARK_SEPARATION,
					sizeWM,
					sizeWM,
					null
				);
				break;
			}
			// Up-right
			case 2:
			{
				g.drawImage(
					wm,
					size - (WATERMARK_SEPARATION + sizeWM),
					WATERMARK_SEPARATION,
					sizeWM,
					sizeWM,
					null
				);
				break;
			}
			// Down-left
			case 3:
			{
				g.drawImage(
					wm,
					WATERMARK_SEPARATION,
					size - (WATERMARK_SEPARATION + sizeWM),
					sizeWM,
					sizeWM,
					null
				);
				break;
			}
			// Down-right
			case 4:
			{
				g.drawImage(
					wm,
					size - (WATERMARK_SEPARATION + sizeWM),
					size - (WATERMARK_SEPARATION + sizeWM),
					sizeWM,
					sizeWM,
					null
				);
				break;
			}
			// No watermark
			default:
			{
				break;
			}
		}
		
		// Finalizar dibujo
		g.dispose();
		
		return res;
	}
	
	/*
	 * Recibe una imagen y la reajusta para que su tamaño máximo (ancho o alto) sea 'size'.
	 */
	private BufferedImage resizeImageToMax( BufferedImage original, int size )
	{
		int newHeight,
			newWidth;
		
		// Ajustar una imagen vertical
		if (original.getHeight() >= original.getWidth())
		{
			newHeight = size;
			newWidth = (int) Math.round(original.getWidth() * (1.0 * size / original.getHeight()));
			
			if (newWidth % 2 == 1) // Acomodar a un múltiplo de 2 para que el centrado sea exacto
				newWidth --;
		}
		// Ajustar una imagen horizontal
		else
		{
			newWidth = size;
			newHeight = (int) Math.round(original.getHeight() * (1.0 * size / original.getWidth()));
			
			if (newHeight % 2 == 1) // Acomodar a un múltiplo de 2 para que el centrado sea exacto
				newHeight --;
		}
		
		// Redimencionar la imagen original al nuevo tamaño adecuado.
		Image tmp = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

}
