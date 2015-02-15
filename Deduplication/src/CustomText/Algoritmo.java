package CustomText;


import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;

import javax.swing.*;
/**
 *
 * @author JOSE VELEZ GOMEZ
 */

public class Algoritmo extends JFrame{
	final private JFrame tituloVentana;
	private JLabel lblTitulo;
	final private CustomerTextField txtNombreArchivo;
	private JButton btnEjecutar;
	private JComboBox cbxAlgoritmo, chbHash;
	private TextArea campoTexto;

	public Algoritmo()
	{
		String [] algoritmo={"Variable-Block","Fixed-Block"};
		String [] hashAlgoritmo={"MD5","SHA1"};
		
	    tituloVentana=new JFrame("Tecnicas de Deduplicacion"); 
	    JDesktopPane escritorio=new JDesktopPane();
	    setLayout(new FlowLayout());
	    
	    lblTitulo = new JLabel("DEDUPLICACION");
	    lblTitulo.setForeground(new Color(54, 188, 171));
	    txtNombreArchivo=new CustomerTextField(20);
	    txtNombreArchivo.setPlaceholder(" Ingrese el nombre del archivo que se le aplicara la deduplicacion...");
	    btnEjecutar=new JButton("EJECUTAR - ALGORITMO");
	    cbxAlgoritmo = new JComboBox(algoritmo);
	    chbHash = new JComboBox(hashAlgoritmo);
	    campoTexto = new TextArea();
	    campoTexto.disable();
	    
	    lblTitulo.setBounds(340, 10, 120, 20);
	    txtNombreArchivo.setBounds(150,50,440,20);
	    cbxAlgoritmo.setBounds(200, 80, 160, 20);
	    chbHash.setBounds(470, 80, 70, 20);
	    btnEjecutar.setBounds(290,120,210,20);
	    campoTexto.setBounds(10, 150, 760, 160);
	    
	    escritorio.add(lblTitulo);
	    escritorio.add(cbxAlgoritmo);
	    escritorio.add(chbHash);
	    escritorio.add(btnEjecutar);
	    escritorio.add(txtNombreArchivo);
	    escritorio.add(campoTexto);
	    
	    tituloVentana.add(escritorio);
	    tituloVentana.setSize(800,350);
	    tituloVentana.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    tituloVentana.setVisible(true);
	    tituloVentana.setLocationRelativeTo(null);
	    
	    btnEjecutar.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent ae) {
	    		campoTexto.setText("");
	    		int[] T={2,4,8,16,32};
	    		String nombreHash = chbHash.getSelectedItem().toString();
	    		campoTexto.setText("");
    			campoTexto.setText("Nombre del archivo: "+txtNombreArchivo.getText().toString()+"\n");
    			
	    		if(cbxAlgoritmo.getSelectedItem().toString().equals("Fixed-Block")){ //Algoritmo AMAÑO FIJO
	        		try
	        		{
	        			MessageDigest md5 = MessageDigest.getInstance(""+nombreHash);
	        			Path path = Paths.get("/home/jose/Escritorio/MetodoProyecto/Archivos/"+txtNombreArchivo.getText().toString());
        				
	        			for (int k = 0; k < T.length; k++ )
	        			{
	        				byte[] dataBytes = Files.readAllBytes(path);
	        				ArrayList<byte[]> arrayDataBytes = new ArrayList<byte[]>();
	        				ArrayList<Integer> tamañoBloque = new ArrayList<Integer>();
	        				int contador = 0,tamaño = T[k];
	        				byte[] md5bytes;
	        				while ((contador + tamaño) <= dataBytes.length) 
	        				{
	        					md5.update(dataBytes, contador, tamaño);
	        					md5bytes = md5.digest();
	        					arrayDataBytes.add(md5bytes);
	        					tamañoBloque.add(tamaño);
	        					contador = contador + tamaño;
	        				}
	        				if (contador < dataBytes.length)
	        				{
	        					md5.update(dataBytes, contador, dataBytes.length-contador);
	        					md5bytes = md5.digest();
	        					arrayDataBytes.add(md5bytes);
	        					tamañoBloque.add(dataBytes.length-contador);
	        				}
	        				ArrayList<String> arrayHexFormatDataBytes = new ArrayList<String>();  
	        				for (int i = 0; i<arrayDataBytes.size(); i++)
	        				{
	        					//convert the byte to hex format
	        					StringBuffer sb = new StringBuffer();
	        					for (int j = 0; j < arrayDataBytes.get(i).length; j++) 
	        					{
	        						sb.append(Integer.toString((arrayDataBytes.get(i)[j] & 0xff) + 0x100, 16).substring(1));
	        					}
	        					arrayHexFormatDataBytes.add(sb.toString());
	        				}
	        				ArrayList<String> arrayHexFormatDataBytesNoRepetidos = new  ArrayList<String>();
	        				ArrayList<Integer> tamañoBloqueNoRepetidos = new  ArrayList<Integer>();
	        				for (int i = 0; i<arrayHexFormatDataBytes.size(); i++)
	        				{
	        					if(arrayHexFormatDataBytesNoRepetidos.contains(arrayHexFormatDataBytes.get(i))==false)
	        					{
	        						arrayHexFormatDataBytesNoRepetidos.add(arrayHexFormatDataBytes.get(i));
	        						tamañoBloqueNoRepetidos.add(tamañoBloque.get(i));
	        					}
	        				}
	        				int acumulador = 0;
	        				for (int i = 0; i < arrayHexFormatDataBytesNoRepetidos.size() ; i++)
	        				{
	        					acumulador = acumulador + tamañoBloqueNoRepetidos.get(i);
	        				}
	        				campoTexto.append("Tamaño Del Bloque: " + tamaño +"  => Tamaño Original: " + dataBytes.length + " bytes"+" => Nuevo Tamaño: " + acumulador + " bytes\n");
	        			}
	        		}
	        		catch (Exception e) 
	        		{
	        			campoTexto.setText(""+e);
	        		}
	    			
	            }else{//Algoritmo Variable
	            	try
	        		{
	        			MessageDigest md5 = MessageDigest.getInstance(""+nombreHash);
	        			Path path = Paths.get("/home/jose/Escritorio/MetodoProyecto/Archivos/"+txtNombreArchivo.getText().toString());
	        			for (int k = 0; k < T.length; k++ )
	        			{
	        				byte[] dataBytes = Files.readAllBytes(path);
	        				ArrayList<byte[]> arrayDataBytes = new ArrayList<byte[]>();
	        				ArrayList<Integer> tamañoBloque = new ArrayList<Integer>();
	        				int aleatorio = (int) (Math.random() *T.length);
	        				int contador = 0,tamaño = T[aleatorio];
	        				byte[] md5bytes;
	        				while ((contador + tamaño) <= dataBytes.length) 
	        				{
	        					md5.update(dataBytes, contador, tamaño);
	        					md5bytes = md5.digest();
	        					arrayDataBytes.add(md5bytes);
	        					tamañoBloque.add(tamaño);
	        					aleatorio = (int) (Math.random() *T.length);
	        					contador = contador + tamaño;
	        					tamaño = T[aleatorio];
	        				}
	        				if (contador < dataBytes.length)
	        				{
	        					md5.update(dataBytes, contador, dataBytes.length-contador);
	        					md5bytes = md5.digest();
	        					arrayDataBytes.add(md5bytes);
	        					tamañoBloque.add(dataBytes.length-contador);
	        				}
	        				ArrayList<String> arrayHexFormatDataBytes = new ArrayList<String>();  
	        				for (int i = 0; i<arrayDataBytes.size(); i++)
	        				{
	        					//convert the byte to hex format
	        					StringBuffer sb = new StringBuffer();
	        					for (int j = 0; j < arrayDataBytes.get(i).length; j++) 
	        					{
	        						sb.append(Integer.toString((arrayDataBytes.get(i)[j] & 0xff) + 0x100, 16).substring(1));
	        					}
	        					arrayHexFormatDataBytes.add(sb.toString());
	        				}
	        				ArrayList<String> arrayHexFormatDataBytesNoRepetidos = new  ArrayList<String>();
	        				ArrayList<Integer> tamañoBloqueNoRepetidos = new  ArrayList<Integer>();
	        				for (int i = 0; i<arrayHexFormatDataBytes.size(); i++)
	        				{
	        					if(arrayHexFormatDataBytesNoRepetidos.contains(arrayHexFormatDataBytes.get(i))==false)
	        					{
	        						arrayHexFormatDataBytesNoRepetidos.add(arrayHexFormatDataBytes.get(i));
	        						tamañoBloqueNoRepetidos.add(tamañoBloque.get(i));
	        					}
	        				}
	        				int acumulador = 0;
	        				for (int i = 0; i < arrayHexFormatDataBytesNoRepetidos.size() ; i++)
	        				{
	        					acumulador = acumulador + tamañoBloqueNoRepetidos.get(i);
	        				}
	        				campoTexto.append("Tamaño Del Bloque: aleatorio->[2,4,8,16,32]" + "  => Tamaño Original: " + dataBytes.length + " bytes"+" => Nuevo Tamaño: " + acumulador + " bytes\n");
	        			}
	        		}
	        		catch (Exception e) 
	        		{
	        			campoTexto.setText(""+e);
	        		}
	            }
	        }
	    });
	}
}



