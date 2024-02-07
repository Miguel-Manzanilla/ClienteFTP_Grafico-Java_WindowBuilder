package ClienteFTPconVentana;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JList;
import java.awt.Color;

public class Prinicpal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTextField txtServer;
	private static JTextField txtUser;
	private static JPasswordField txtClave;
	public static String servidor;
	public static String user;
	public static String pass;
	public static boolean conectar = true;
	private static FTPClient cliente = new FTPClient();
	private static JButton btnSubirFichero;
	private static JLabel lbDirec;
	private static JList list_1;
	private static JButton btnDescargar;
	private static JButton btnCrearCarpeta;
	private static JButton btnEliminarFichero;
	private static JButton btnEliminarCarpeta;
	private static JButton btnSalir;
	private static String carpetaPrincipal = "/";
	private static JButton btnSalirDirec;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Prinicpal frame = new Prinicpal();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					btnCrearCarpeta.setEnabled(false);
					btnDescargar.setEnabled(false);
					btnEliminarCarpeta.setEnabled(false);
					btnEliminarFichero.setEnabled(false);
					btnSubirFichero.setEnabled(false);
					btnSalirDirec.setEnabled(false);
					list_1.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 2) {
								
								if (list_1.getSelectedValue() == "" || list_1.getSelectedValue() == null) {
									try {
										System.out.println("CARPETA ACTUAL: " + carpetaPrincipal);
										cliente.changeWorkingDirectory("..");
										carpetaPrincipal = cliente.printWorkingDirectory();
										System.out.println("CARPETA ATRAS: " + carpetaPrincipal);
										MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									if (carpetaPrincipal.equals("/")) {
										btnSalirDirec.setEnabled(false);
									}else {
										btnSalirDirec.setEnabled(true);
									}
								} else {
									String archivo = list_1.getSelectedValue().toString().split("-")[0].trim();
									if (archivo.equals("Directorio")) {
										System.out.println("DIRECTORIO SIGUIENTE: " + carpetaPrincipal
												+ list_1.getSelectedValue().toString().split(">")[1]);
										if (carpetaPrincipal.equals("/")) {
											carpetaPrincipal = carpetaPrincipal
													+ list_1.getSelectedValue().toString().split(">")[1].trim();
										} else {
											carpetaPrincipal = carpetaPrincipal + "/"
													+ list_1.getSelectedValue().toString().split(">")[1].trim();
										}

										try {
											MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									if (carpetaPrincipal.equals("/")) {
										btnSalirDirec.setEnabled(false);
									}else {
										btnSalirDirec.setEnabled(true);
									}
								}
								
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Prinicpal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 675, 446);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 153, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("SERVIDOR:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(10, 10, 127, 13);
		contentPane.add(lblNewLabel);

		txtServer = new JTextField();
		txtServer.setBackground(new Color(192, 192, 192));
		txtServer.setBounds(102, 9, 163, 19);
		contentPane.add(txtServer);
		txtServer.setColumns(10);

		JLabel lblUsuario = new JLabel("USUARIO:");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsuario.setBounds(10, 39, 127, 13);
		contentPane.add(lblUsuario);

		txtUser = new JTextField();
		txtUser.setBackground(new Color(192, 192, 192));
		txtUser.setBounds(102, 38, 163, 19);
		contentPane.add(txtUser);
		txtUser.setColumns(10);

		txtClave = new JPasswordField();
		txtClave.setBackground(new Color(192, 192, 192));
		txtClave.setBounds(402, 38, 249, 19);
		contentPane.add(txtClave);

		JLabel lblNewLabel_1 = new JLabel("CLAVE:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(329, 41, 63, 13);
		contentPane.add(lblNewLabel_1);

		JButton btnNewButton = new JButton("CONECTAR");
		btnNewButton.setBackground(Color.PINK);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (conectar) {
					try {
						servidor = txtServer.getText();
						user = txtUser.getText();
						pass = txtClave.getText();
						cliente.connect(servidor);
						boolean login;
						login = cliente.login(user, pass);
						if (login) {
							JOptionPane.showMessageDialog(null, "CONEXION REALIZADA CON EXITO");
							MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
							btnNewButton.setText("DESCONECTAR");
							conectar = false;
						} else {
							JOptionPane.showMessageDialog(null, "ERROR EN LAS CREDENCIALES");
						}
						btnCrearCarpeta.setEnabled(true);
						btnDescargar.setEnabled(true);
						btnEliminarCarpeta.setEnabled(true);
						btnEliminarFichero.setEnabled(true);
						btnSubirFichero.setEnabled(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					try {
						JOptionPane.showMessageDialog(null, "DESCONECTADO CON EXITO");
						txtClave.setText("");
						txtServer.setText("");
						txtUser.setText("");
						list_1.setListData(new String[0]);
						carpetaPrincipal = "/";
						cliente.disconnect();
						btnNewButton.setText("CONECTAR");
						conectar = true;
						btnCrearCarpeta.setEnabled(false);
						btnDescargar.setEnabled(false);
						btnEliminarCarpeta.setEnabled(false);
						btnEliminarFichero.setEnabled(false);
						btnSubirFichero.setEnabled(false);
						btnSalirDirec.setEnabled(false);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		});
		btnNewButton.setBounds(329, 8, 322, 21);
		contentPane.add(btnNewButton);

		btnSubirFichero = new JButton("SUBIR FICHERO");
		btnSubirFichero.setBackground(new Color(128, 255, 128));
		btnSubirFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!conectar) {
					try {
						JFileChooser fileChooser = new JFileChooser();
						int seleccion = fileChooser.showSaveDialog(contentPane);
						File fichero = fileChooser.getSelectedFile();
						cliente.setFileType(FTP.BINARY_FILE_TYPE);
						BufferedInputStream in = new BufferedInputStream(new FileInputStream(fichero));
						if (cliente.storeFile(fichero.getName(), in)) {
							JOptionPane.showMessageDialog(null,
									"FICHERO: " + fichero.getName() + " ==> SUBIDO AL SERVER");
							list_1.clearSelection();

							MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
						} else {
							JOptionPane.showMessageDialog(null, "ERROR AL SUBIR EL FICHERO");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "CONEXION NO REALIZADA");
				}
			}

		});
		btnSubirFichero.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSubirFichero.setBounds(402, 140, 249, 35);
		contentPane.add(btnSubirFichero);

		list_1 = new JList();
		list_1.setBackground(new Color(192, 192, 192));
		list_1.setBounds(10, 140, 382, 260);
		contentPane.add(list_1);

		lbDirec = new JLabel("");
		lbDirec.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbDirec.setBounds(10, 77, 382, 13);
		contentPane.add(lbDirec);

		btnDescargar = new JButton("DESCARGAR FICHERO");
		btnDescargar.setBackground(new Color(255, 255, 128));
		btnDescargar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (String.valueOf(list_1.getSelectedValue()).contains("Fichero")) {
		            JFileChooser fileChooser = new JFileChooser();
		            int seleccion = fileChooser.showSaveDialog(contentPane);
		            if (seleccion == JFileChooser.APPROVE_OPTION) {
		                File archivoSeleccionado = fileChooser.getSelectedFile();
		                String nombreArchivo = String.valueOf(list_1.getSelectedValue()).split(">")[1].trim();
		                String rutaArchivoLocal = archivoSeleccionado.getAbsolutePath();
		                try {
		                    boolean descargado = cliente.retrieveFile(nombreArchivo, new BufferedOutputStream(new FileOutputStream(rutaArchivoLocal)));
		                    if (descargado) {
		                        JOptionPane.showMessageDialog(null, "FICHERO DESCARGADO CON ÉXITO");
		                    } else {
		                        JOptionPane.showMessageDialog(null, "ERROR AL DESCARGAR EL FICHERO");
		                    }
		                } catch (IOException ex) {
		                    ex.printStackTrace();
		                }
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "NO HAS SELECCIONADO UN FICHERO");
		        }
		    }
		});
		btnDescargar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDescargar.setBounds(402, 185, 249, 35);
		contentPane.add(btnDescargar);

		btnEliminarFichero = new JButton("ELIMINAR FICHERO");
		btnEliminarFichero.setBackground(new Color(255, 0, 0));
		btnEliminarFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (String.valueOf(list_1.getSelectedValue()).contains("Fichero")) {
					int confirmado = JOptionPane.showConfirmDialog(null, "¿DESEA BORRARLO?");
					if (JOptionPane.OK_OPTION == confirmado) {
						try {
							JOptionPane.showMessageDialog(null, "BORRADO CORRECTAMENTE");
							cliente.deleteFile((String.valueOf(list_1.getSelectedValue()).split(">")[1]));
							MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "NO BORRADO");
					}
				} else {
					JOptionPane.showMessageDialog(null, "NO HAS SELECIONADO UN FICHERO");
				}
			}
		});
		btnEliminarFichero.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnEliminarFichero.setBounds(402, 230, 249, 35);
		contentPane.add(btnEliminarFichero);

		btnCrearCarpeta = new JButton("CREAR CARPETA");
		btnCrearCarpeta.setBackground(new Color(128, 255, 128));
		btnCrearCarpeta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!conectar) {
					try {
						cliente.makeDirectory(JOptionPane.showInputDialog("INTRODUCE EL NOMBRE"));
						MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "CONEXION NO REALIZADA");
				}

			}
		});
		btnCrearCarpeta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCrearCarpeta.setBounds(402, 275, 249, 35);
		contentPane.add(btnCrearCarpeta);

		btnEliminarCarpeta = new JButton("ELIMINAR CARPETA");
		btnEliminarCarpeta.setBackground(new Color(255, 0, 0));
		btnEliminarCarpeta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (String.valueOf(list_1.getSelectedValue()).contains("Directorio")) {
					int confirmado = JOptionPane.showConfirmDialog(null, "¿DESEA BORRARLO?");
					if (JOptionPane.OK_OPTION == confirmado) {
						try {
							JOptionPane.showMessageDialog(null, "BORRADO CORRECTAMENTE");
							String direc = (String.valueOf(list_1.getSelectedValue()).split(">")[1]);
							cliente.removeDirectory(direc);
							MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "NO BORRADO");
					}
				} else {
					JOptionPane.showMessageDialog(null, "NO HAS SELECIONADO UN DIRECTORIO");
				}
			}
		});
		btnEliminarCarpeta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnEliminarCarpeta.setBounds(402, 320, 249, 35);
		contentPane.add(btnEliminarCarpeta);

		btnSalir = new JButton("SALIR");
		btnSalir.setBackground(Color.PINK);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSalir.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSalir.setBounds(402, 365, 249, 35);
		contentPane.add(btnSalir);

		btnSalirDirec = new JButton("SALIR AL DIRECTORIO ANTERIOR");
		btnSalirDirec.setBackground(Color.PINK);
		btnSalirDirec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("CARPETA ACTUAL: " + carpetaPrincipal);
					cliente.changeWorkingDirectory("..");
					carpetaPrincipal = cliente.printWorkingDirectory();
					System.out.println("CARPETA ATRAS: " + carpetaPrincipal);
					MostrarDirectorio(carpetaPrincipal, list_1, lbDirec);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (carpetaPrincipal.equals("/")) {
					btnSalirDirec.setEnabled(false);
				}else {
					btnSalirDirec.setEnabled(true);
				}
			}
		});
		btnSalirDirec.setBounds(10, 109, 238, 21);
		contentPane.add(btnSalirDirec);
	}

	private static void MostrarDirectorio(String carpetaPrincipal, JList list, JLabel label) throws IOException {
		label.setText("DIRECTORIO ACTUAL: " + carpetaPrincipal);
		cliente.changeWorkingDirectory(carpetaPrincipal);
		FTPFile[] files = cliente.listFiles();
		String tipos[] = { "Fichero", "Directorio", "Enlace" };
		String lista[] = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			lista[i] = (tipos[files[i].getType()] + "-->" + files[i].getName() + "\n");
		}
		list.setListData(lista);
	}
}
