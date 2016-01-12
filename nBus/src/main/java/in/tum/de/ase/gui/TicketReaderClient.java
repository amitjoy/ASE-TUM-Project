package in.tum.de.ase.gui;

import static in.tum.de.ase.db.TicketsHandler.insertTicket;
import static in.tum.de.ase.db.TicketsHandler.isValidatedTicket;
import static in.tum.de.ase.util.TicketParser.parse;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import in.tum.de.ase.db.DBInitializer;
import in.tum.de.ase.exception.NonParseableTicketException;
import in.tum.de.ase.model.Eticket;

/**
 * Main Reader Application
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class TicketReaderClient extends JFrame implements Runnable, ThreadFactory {

	private static final long serialVersionUID = 6441489157408381878L;

	static {
		DBInitializer.getInstance().setUp();
	}

	public static void centreWindow(final Window frame) {
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		final int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new TicketReaderClient();
	}

	private final Executor executor = Executors.newCachedThreadPool();

	private WebcamPanel panel = null;

	private Webcam webcam = null;

	public TicketReaderClient() {
		super();

		this.setLayout(new FlowLayout());
		this.setTitle("==== Scan Ticket =====");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Dimension size = WebcamResolution.QVGA.getSize();

		this.webcam = Webcam.getWebcams().get(0);
		this.webcam.setViewSize(size);

		this.panel = new WebcamPanel(this.webcam);
		this.panel.setPreferredSize(size);

		this.add(this.panel);

		this.pack();
		this.setVisible(true);
		centreWindow(this);
		this.executor.execute(this);
	}

	@Override
	public Thread newThread(final Runnable r) {
		final Thread t = new Thread(r, "ticket-scanner-worker");
		t.setDaemon(true);
		return t;
	}

	@Override
	public void run() {
		do {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			Result result = null;
			BufferedImage image = null;

			if (this.webcam.isOpen()) {

				if ((image = this.webcam.getImage()) == null) {
					continue;
				}

				final LuminanceSource source = new BufferedImageLuminanceSource(image);
				final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (final NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			if (result != null) {
				final String ticketQrCode = result.getText();
				try {
					final Eticket eticket = parse(ticketQrCode);
					if (eticket != null) {
						if (!isValidatedTicket(eticket.getTicketId())) {
							insertTicket(eticket);
							showMessageDialog(null, "Ticket is Validated", "Ticket Validation", INFORMATION_MESSAGE);

						} else {
							showMessageDialog(null, "Provided Ticket is already validated", "Ticket Validation",
									ERROR_MESSAGE);
						}

					}
				} catch (final NonParseableTicketException e) {
					showMessageDialog(null, "Invalid Ticket Provided", "Ticket Validation", ERROR_MESSAGE);
				}
			}

		} while (true);
	}
}
