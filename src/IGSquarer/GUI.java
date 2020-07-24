package IGSquarer;



import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;



public class GUI extends JFrame
{
	private final String	PROGRAM_NAME	= "IGSquarer",
							PROGRAM_VERSION	= "2.0",
							PROGRAM_AUTHOR	= "M. Nicolás Vega";
	
	private final int		SIZE_MAX	= 1920,
							SIZE_MIN	= 640;

	private JPanel panel;
	private JLabel lblStatus;
	private JTextField txtInput;
	private JTextField txtOutput;
	private JTextField txtColor;
	private JButton btnProcess;
	private JLabel lblSize;
	private JLabel lblInput;
	private JLabel lblColor;
	private JLabel lblOutput;
	private JTextField txtSize;
	private JButton btnColor;
	private JButton btnSearchSrc;
	private JButton btnSearchDst;
	
	private JCheckBox chkWatermark;
	private JPanel panelWM;
	private JLabel lblWatermark;
	private JCheckBox chkUL;
	private JCheckBox chkUR;
	private JCheckBox chkDL;
	private JCheckBox chkDR;
	private ButtonGroup groupWM;
	private JLabel lblSizeWM;
	private JTextField txtSizeWM;
	private JLabel lblSrcWM;
	private JTextField txtSrcWM;
	private JButton btnSearchWM;

	private JCheckBox chkBG;
	private JLabel lblSrcBG;
	private JTextField txtSrcBG;
	private JButton btnSearchBG;
	
	private JSeparator separator1;
	private JSeparator separator2;
	private JSeparator separator3;
	private JSeparator separator4;
	private JSeparator separator5;
	private JSeparator separator6;
	private JSeparator separator7;
	private JSeparator separator8;
	
	private Configuration cfg;
	private Processor p;
	private Thread t;
	private GUI me;


	
	
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					GUI frame = new GUI();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	
	public GUI()
	{
		me = this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 386);
		setLocationRelativeTo( null );
		setResizable( false );
		setTitle(PROGRAM_NAME + " " + PROGRAM_VERSION + " - por " + PROGRAM_AUTHOR);

		createCoreComponents( );
		createSourceAndDestComponents( );
		createColorAndSizeComponents( );
		createBackgroundComponents( );
		createSeparators( );
		createWatermarkComponents( );

		File cfgFile = new File("IGSquarer.cfg");
		cfg = new Configuration( cfgFile );
		if (cfgFile.exists())
		{
			cfg.load( );
			loadDataToComponents( );
		}

		updateStatus( "-" );
	}

	
	
	private void createCoreComponents( )
	{
		panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		btnProcess = new JButton("Procesar");
		btnProcess.setBackground(new Color(152, 251, 152));
		btnProcess.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				processButton( );
			}
		});
		btnProcess.setBounds(460, 250, 125, 70);
		panel.add(btnProcess);
		
		lblStatus = new JLabel("");
		lblStatus.setOpaque(true);
		lblStatus.setBackground(Color.BLACK);
		lblStatus.setForeground(Color.GREEN);
		lblStatus.setFont(new Font("Consolas", Font.BOLD, 13));
		lblStatus.setBounds(0, 329, 734, 28);
		panel.add(lblStatus);
	}
	
	private void createColorAndSizeComponents( )
	{
		lblColor = new JLabel("Color de fondo:");
		lblColor.setBounds(10, 250, 130, 20);
		panel.add(lblColor);
		
		txtColor = new JTextField();
		txtColor.setText("#FFFFFF");
		txtColor.setBounds(10, 270, 130, 20);
		panel.add(txtColor);
		txtColor.setColumns(10);
		
		btnColor = new JButton("Seleccionar (HEX)");
		btnColor.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnColor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					Desktop.getDesktop().browse( new URL( "https://www.google.com/search?q=color+picker" ).toURI() );
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		btnColor.setBounds(10, 300, 130, 20);
		panel.add(btnColor);
		
		lblSize = new JLabel("Tama\u00F1o im\u00E1genes:");
		lblSize.setBounds(160, 250, 130, 20);
		panel.add(lblSize);
		
		txtSize = new JTextField();
		txtSize.setText("1080");
		txtSize.setBounds(160, 270, 130, 20);
		panel.add(txtSize);
		txtSize.setColumns(10);
	}
	
	private void createSourceAndDestComponents( )
	{
		lblInput = new JLabel("Ubicaci\u00F3n de las im\u00E1genes (originales):");
		lblInput.setBounds(10, 10, 500, 20);
		panel.add(lblInput);
		
		txtInput = new JTextField();
		txtInput.setBounds(10, 30, 490, 20);
		panel.add(txtInput);
		txtInput.setColumns(10);
		
		lblOutput = new JLabel("Ubicaci\u00F3n de las im\u00E1genes procesadas (resultado):");
		lblOutput.setBounds(10, 70, 500, 20);
		panel.add(lblOutput);
		
		txtOutput = new JTextField();
		txtOutput.setBounds(10, 90, 490, 20);
		panel.add(txtOutput);
		txtOutput.setColumns(10);
		
		lblSrcWM = new JLabel("Ubicaci\u00F3n de la marca de agua:");
		lblSrcWM.setBounds(10, 130, 500, 20);
		panel.add(lblSrcWM);
		
		txtSrcWM = new JTextField();
		txtSrcWM.setColumns(10);
		txtSrcWM.setBounds(10, 150, 490, 20);
		panel.add(txtSrcWM);
		
		lblSrcBG = new JLabel("Ubicaci\u00F3n de imagen de fondo:");
		lblSrcBG.setBounds(10, 190, 500, 20);
		panel.add(lblSrcBG);
		
		txtSrcBG = new JTextField();
		txtSrcBG.setColumns(10);
		txtSrcBG.setBounds(10, 210, 490, 20);
		panel.add(txtSrcBG);
		
		btnSearchSrc = new JButton("Buscar");
		btnSearchSrc.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle( "Carpeta de Origen" );
				chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				chooser.setAcceptAllFileFilterUsed(false);
				
				int returnVal = chooser.showOpenDialog( me );
				if (returnVal == JFileChooser.APPROVE_OPTION)
					txtInput.setText( chooser.getSelectedFile().getAbsolutePath() );
			}
		});
		btnSearchSrc.setBounds(510, 30, 75, 20);
		panel.add(btnSearchSrc);
		
		btnSearchDst = new JButton("Buscar");
		btnSearchDst.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle( "Carpeta de Destino" );
				chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				chooser.setAcceptAllFileFilterUsed(false);
				
				int returnVal = chooser.showOpenDialog( me );
				if (returnVal == JFileChooser.APPROVE_OPTION)
					txtOutput.setText( chooser.getSelectedFile().getAbsolutePath() );
			}
		});
		btnSearchDst.setBounds(510, 90, 75, 20);
		panel.add(btnSearchDst);

		btnSearchWM = new JButton("Buscar");
		btnSearchWM.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG, GIF", "jpg", "png", "gif");
				
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle( "Imagen para Marca de Agua" );
				chooser.setFileFilter( filter );
				chooser.setAcceptAllFileFilterUsed(false);
				
				int returnVal = chooser.showOpenDialog( me );
				if (returnVal == JFileChooser.APPROVE_OPTION)
					txtSrcWM.setText( chooser.getSelectedFile().getAbsolutePath() );
			}
		});
		btnSearchWM.setBounds(510, 150, 75, 20);
		panel.add(btnSearchWM);
		
		btnSearchBG = new JButton("Buscar");
		btnSearchBG.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG, GIF", "jpg", "png", "gif");
				
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle( "Imagen de Fondo" );
				chooser.setFileFilter( filter );
				chooser.setAcceptAllFileFilterUsed(false);
				
				int returnVal = chooser.showOpenDialog( me );
				if (returnVal == JFileChooser.APPROVE_OPTION)
					txtSrcBG.setText( chooser.getSelectedFile().getAbsolutePath() );
			}
		});
		btnSearchBG.setBounds(510, 210, 75, 20);
		panel.add(btnSearchBG);
	}
	
	private void createBackgroundComponents( )
	{
		chkBG = new JCheckBox("<html>Utilizar imagen de<br/>fondo <b>(?)<b/>");
		chkBG.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				enableBGComponents( chkBG.isSelected() );
			}
		});
		chkBG.setToolTipText("Se ajustar\u00E1 su tama\u00F1o a un cuadrado de la resoluci\u00F3n de la imagen final.");
		chkBG.setVerticalAlignment(SwingConstants.TOP);
		chkBG.setVerticalTextPosition(SwingConstants.TOP);
		chkBG.setOpaque(false);
		chkBG.setBounds(310, 250, 130, 43);
		panel.add(chkBG);
		
		enableBGComponents( false );
	}
	
	private void createSeparators( )
	{
		separator1 = new JSeparator();
		separator1.setForeground(Color.BLACK);
		separator1.setBounds(10, 60, 574, 2);
		panel.add(separator1);
		
		separator2 = new JSeparator();
		separator2.setForeground(Color.BLACK);
		separator2.setBounds(10, 120, 574, 2);
		panel.add(separator2);
		
		separator3 = new JSeparator();
		separator3.setForeground(Color.BLACK);
		separator3.setOrientation(SwingConstants.VERTICAL);
		separator3.setBounds(150, 250, 2, 70);
		panel.add(separator3);
		
		separator4 = new JSeparator();
		separator4.setOrientation(SwingConstants.VERTICAL);
		separator4.setForeground(Color.BLACK);
		separator4.setBounds(300, 250, 2, 70);
		panel.add(separator4);
		
		separator5 = new JSeparator();
		separator5.setForeground(Color.BLACK);
		separator5.setOrientation(SwingConstants.VERTICAL);
		separator5.setBounds(594, 10, 2, 310);
		panel.add(separator5);
		
		separator6 = new JSeparator();
		separator6.setForeground(Color.BLACK);
		separator6.setBounds(10, 180, 574, 2);
		panel.add(separator6);
		
		separator7 = new JSeparator();
		separator7.setForeground(Color.BLACK);
		separator7.setBounds(11, 240, 574, 2);
		panel.add(separator7);
		
		separator8 = new JSeparator();
		separator8.setOrientation(SwingConstants.VERTICAL);
		separator8.setForeground(Color.BLACK);
		separator8.setBounds(450, 250, 2, 70);
		panel.add(separator8);
	}
	
	private void createWatermarkComponents( )
	{
		chkWatermark = new JCheckBox("Marca de Agua");
		chkWatermark.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				enableWMComponents( chkWatermark.isSelected() );
			}
		});
		chkWatermark.setOpaque(false);
		chkWatermark.setBounds(603, 10, 125, 23);
		panel.add(chkWatermark);
		
		panelWM = new JPanel();
		panelWM.setBounds(606, 35, 118, 117);
		panel.add(panelWM);
		panelWM.setLayout(null);
		
		lblWatermark = new JLabel("Posición");
		lblWatermark.setHorizontalAlignment(SwingConstants.CENTER);
		lblWatermark.setBounds(0, 0, 118, 117);
		panelWM.add(lblWatermark);
		
		txtSizeWM = new JTextField();
		txtSizeWM.setText("100");
		txtSizeWM.setColumns(10);
		txtSizeWM.setBounds(604, 179, 120, 20);
		panel.add(txtSizeWM);

		chkUL = new JCheckBox("");
		chkUL.setHorizontalAlignment(SwingConstants.CENTER);
		chkUL.setBounds(0, 0, 25, 25);
		panelWM.add(chkUL);
		
		chkUR = new JCheckBox("");
		chkUR.setHorizontalAlignment(SwingConstants.CENTER);
		chkUR.setBounds(93, 0, 25, 25);
		panelWM.add(chkUR);
		
		chkDL = new JCheckBox("");
		chkDL.setHorizontalAlignment(SwingConstants.CENTER);
		chkDL.setBounds(0, 92, 25, 25);
		panelWM.add(chkDL);
		
		chkDR = new JCheckBox("");
		chkDR.setHorizontalAlignment(SwingConstants.CENTER);
		chkDR.setBounds(93, 92, 25, 25);
		panelWM.add(chkDR);
		
		groupWM = new ButtonGroup();
		groupWM.add( chkUL );
		groupWM.add( chkUR );
		groupWM.add( chkDL );
		groupWM.add( chkDR );
		
		lblSizeWM = new JLabel("Tama\u00F1o:");
		lblSizeWM.setBounds(606, 160, 118, 20);
		panel.add(lblSizeWM);
		
		enableWMComponents( false );
	}
	
	
	
	private void enableComponents( boolean status )
	{
		/*for (Component c : panel.getComponents())
		{
			c.setEnabled( status );
		}
		btnProcess.setEnabled( true );*/
		
		/*txtInput.setEnabled( status );
		txtOutput.setEnabled( status );
		txtColor.setEnabled( status );
		txtSize.setEnabled( status );
		btnColor.setEnabled( status );*/
	}
	
	private void enableBGComponents( boolean status )
	{
		txtSrcBG.setEnabled( status );
		lblSrcBG.setEnabled( status );
		btnSearchBG.setEnabled( status );
	}
	
	private void enableWMComponents( boolean status )
	{
		chkUL.setEnabled( status );
		chkUR.setEnabled( status );
		chkDL.setEnabled( status );
		chkDR.setEnabled( status );
		
		lblSizeWM.setEnabled( status );
		txtSizeWM.setEnabled( status );
		
		lblSrcWM.setEnabled( status );
		txtSrcWM.setEnabled( status );
		btnSearchWM.setEnabled( status );
	}
	
	
	
	public void updateStatus( String action )
	{
		lblStatus.setText(" Estado: " + action);
	}
	
	public void finishedParsing( boolean success )
	{
		if (success)
			JOptionPane.showMessageDialog(me, "Proceso finalizado.", "Notificación", JOptionPane.WARNING_MESSAGE);
		else
			JOptionPane.showMessageDialog(me, "Proceso detenido.", "Notificación", JOptionPane.WARNING_MESSAGE);

		p.stop();
		t.interrupt();
		t = null;
		
		btnProcess.setText("Procesar");
		enableComponents( true );
	}
	
	
	
	private void processButton( )
	{
		// COMENZAR
		if (t == null)
		{
			boolean valid_input		= false,
					valid_output	= false,
					valid_color		= false,
					valid_size		= false,
					valid_watermark	= false,
					valid_bg		= false;
			
			Color	color_res		= Color.pink;
			String	error_msg		= "<html><b>Se hallaron los siguientes errores:</b><br/>";
			
			// Verificación carpeta origen
			File src = new File( txtInput.getText() );
			if (src.exists())
			{
				if (src.isDirectory())
				{
					valid_input = true;
				}
				else
					error_msg += "<br/>- La ubicación de origen no es una carpeta.";
			}
			else
				error_msg += "<br/>- La ubicación de origen no existe.";
			
			// Verificación carpeta destino
			File dst = new File( txtOutput.getText() );
			if (dst.exists())
			{
				if (dst.isDirectory())
				{
					if ( !dst.equals(src) )
					{
						if ( dst.listFiles().length == 0 )
							valid_output = true;
						else
							error_msg += "<br/>- La ubicación de destino no está vacía.";
					}
					else
						error_msg += "<br/>- Las ubicaciones destino y origen son la misma.";
				}
				else
					error_msg += "<br/>- La ubicación de destino no es una carpeta.";
			}
			else
				error_msg += "<br/>- La ubicación de destino no existe.";
			
			// Verificación color
			String color = txtColor.getText();
			if (color.charAt(0) == '#')
				color = color.substring(1);
			
			color = color.toUpperCase();
			if (color.length() == 6)
			{
				valid_color = true;
				
				int i;
				char c;
				for (i = 0; i < 6; i ++)
				{
					c = color.charAt(i);
					if (! ((c >= 'A' && c <= 'F') || (c >= '0' && c <= '9')) )
					{
						error_msg += "<br/>- El color es inválido (debe ser hexadecimal).";
						valid_color = false;
						break;
					}
				}
				
				if (valid_color)
					color_res = new Color( Integer.valueOf(color, 16) );
			}
			else
				error_msg += "<br/>- El color es inválido (debe ser hexadecimal de 6 dígitos).";
			
			// Verificación tamaño
			int size = -1;
			try
			{
				size = Integer.parseInt( txtSize.getText() );
				
				if (size >= SIZE_MIN && size <= SIZE_MAX)
				{
					valid_size = true;
				}
				else
					error_msg += "<br/>- El tamaño de imagen está fuera de rango; válido desde " + SIZE_MIN + " hasta " + SIZE_MAX + " píxeles.";
			}
			catch (Exception e)
			{
				error_msg += "<br/>- El tamaño de la imagen debe ser un número.";
			}
			
			// Verificación de marca de agua
			int sizeWM = -1;
			int wmPos = 0;
			BufferedImage wm = null;
			if (chkWatermark.isSelected())
			{
				try
				{
					sizeWM = Integer.parseInt( txtSizeWM.getText() );
					
					if (sizeWM > (size / 5) || sizeWM < 10)
						error_msg += "<br/>- El tamaño de la marca de agua está fuera de rango; válido desde 10 hasta " + (size / 5) + " (20% de la resolución final) píxeles.";
					else
					{
						if		(chkUL.isSelected()) wmPos = 1;
						else if (chkUR.isSelected()) wmPos = 2;
						else if (chkDL.isSelected()) wmPos = 3;
						else if (chkDR.isSelected()) wmPos = 4;
						
						valid_watermark = true;
					}
				}
				catch (Exception e)
				{
					error_msg += "<br/>- El tamaño de la marca de agua debe ser un número.";
				}

				File srcWM = new File( txtSrcWM.getText() );
				if (valid_watermark)
				{
					valid_watermark = false;
					
					if (srcWM.exists())
					{
						if (srcWM.isFile())
						{
							try
							{
								wm = ImageIO.read( srcWM );
							}
							catch (Exception e)
							{
								error_msg += "<br/>- La ubicación de la marca de agua no es un archivo de imagen.";
							}
							
							if (wm != null)
								valid_watermark = true;
						}
						else
							error_msg += "<br/>- La ubicación de la marca de agua no es un archivo.";
					}
					else
						error_msg += "<br/>- La ubicación de la marca de agua no existe.";
				}
			}
			else
				valid_watermark = true;
			
			// Verificar Imagen de Fondo
			BufferedImage fondo = null;
			if (chkBG.isSelected())
			{
				File srcFondo = new File( txtSrcBG.getText() );
				
				if (srcFondo.exists())
				{
					if (srcFondo.isFile())
					{
						try
						{
							fondo = ImageIO.read( srcFondo );
							
							if (fondo.getWidth() < (size / 5) || fondo.getHeight() < (size / 5))
							{
								error_msg += "<br/>- La imagen de fondo debe tener un tamaño mínimo de " + (size / 5) + " píxeles (20% de la resolución final).";
								fondo = null;
							}
						}
						catch (Exception e)
						{
							error_msg += "<br/>- La ubicación de la imagen de fondo no es un archivo de imagen.";
						}
						
						if (fondo != null)
							valid_bg = true;
					}
					else
						error_msg += "<br/>- La ubicación de la imagen de fondo no es un archivo.";
				}
				else
					error_msg += "<br/>- La ubicación de la imagen de fondo no existe.";
			}
			else
				valid_bg = true;
			
			// Finalmente si todo está bien
			if (valid_input && valid_output && valid_color && valid_size && valid_watermark && valid_bg)
			{
				prepareDataSave( wmPos );
				cfg.save();
				
				// Se corrigen los directorios
				String	szSrc = txtInput.getText(),
						szDst = txtOutput.getText();
				
				szSrc = szSrc.replace('\\', '/');
				szDst = szDst.replace('\\', '/');
				
				if (!szSrc.endsWith("/"))
					szSrc += "/";
				
				if (!szDst.endsWith("/"))
					szDst += "/";

				enableComponents( false );
				btnProcess.setText("Detener");
				p = new Processor( szSrc, szDst, size, color_res, fondo, wm, sizeWM, wmPos, me);
				t = new Thread( p );
				t.start();
			}
			// Mostrar errores
			else
			{
				JOptionPane.showMessageDialog(me, error_msg, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		// DETENER
		else
		{					
			finishedParsing( false );
		}
	}
	
	private void prepareDataSave( int wmPos )
	{
		cfg.setValue("FOLDER_SRC",	txtInput.getText());
		cfg.setValue("FOLDER_DST",	txtOutput.getText());

		cfg.setValue("IMG_SIZE",	txtSize.getText());

		cfg.setValue("FILE_WM",		txtSrcWM.getText());
		cfg.setValue("USE_WM",		"" + chkWatermark.isSelected());
		cfg.setValue("WM_POS",		"" + wmPos);
		cfg.setValue("WM_SIZE",		txtSizeWM.getText());

		cfg.setValue("FILE_BG",		txtSrcBG.getText());
		cfg.setValue("USE_BG",		"" + chkBG.isSelected());
		cfg.setValue("BG_COLOR",	txtColor.getText());
	}
	
	private void loadDataToComponents( )
	{		
		txtInput.setText( cfg.getValue("FOLDER_SRC") );
		txtOutput.setText( cfg.getValue("FOLDER_DST") );
		
		txtSize.setText( cfg.getValue("IMG_SIZE") );
		
		txtSrcWM.setText( cfg.getValue("FILE_WM") );
		if (cfg.getValue("USE_WM").equalsIgnoreCase("TRUE"))
		{
			chkWatermark.setSelected(true);
			enableWMComponents(true);
		}
		switch (Integer.parseInt(cfg.getValue("WM_POS")))
		{
			case 1:
			{
				chkUL.setSelected(true);
				break;
			}
			case 2:
			{
				chkUR.setSelected(true);
				break;
			}
			case 3:
			{
				chkDL.setSelected(true);
				break;
			}
			case 4:
			{
				chkDR.setSelected(true);
				break;
			}
		}
		txtSizeWM.setText( cfg.getValue("WM_SIZE") );
		
		txtSrcBG.setText( cfg.getValue("FILE_BG") );
		if (cfg.getValue("USE_BG").equalsIgnoreCase("TRUE"))
		{
			chkBG.setSelected(true);
			enableBGComponents(true);
		}
		txtColor.setText( cfg.getValue("BG_COLOR") );
	}

}
