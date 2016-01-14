/*******************************************************************************
 * Copyright 2016 Amit Kumar Mondal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package in.tum.de.ase.gui;

import static in.tum.de.ase.constants.Constants.DIALOG_BOX_HEADER;
import static in.tum.de.ase.constants.Constants.INVALID_TICKET_MSG;
import static in.tum.de.ase.constants.Constants.VALIDATED_MSG;
import static in.tum.de.ase.db.TicketsHandler.insertTicket;
import static in.tum.de.ase.db.TicketsHandler.isValidatedTicket;
import static in.tum.de.ase.util.TicketParser.isValidTicket;
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

import in.tum.de.ase.constants.Constants;
import in.tum.de.ase.exception.NonParseableTicketException;
import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.observer.controller.Controller;
import in.tum.de.ase.observers.ParseObserver;

/**
 * Main Reader Application to scan ticket QR Code
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class TicketReaderGUI extends JFrame implements Runnable, ThreadFactory {

	/**
	 * Serialisation UID
	 */
	private static final long serialVersionUID = 6441489157408381878L;

	// Setting look and feel for the application
	static {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Centers the provided window
	 */
	public static void centreWindow(final Window frame) {
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		final int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	/**
	 * Opens the QR Code Reader
	 */
	public static void openReader() {

		new TicketReaderGUI();
	}

	/**
	 * Thread Executor to be used for handling ticket reading
	 */
	private final Executor executor = Executors.newCachedThreadPool();

	/**
	 * WebCam Panel Reference
	 */
	private WebcamPanel panel = null;

	/**
	 * WebCam Reference
	 */
	private Webcam webcam = null;

	/**
	 * Constructor
	 */
	private TicketReaderGUI() {
		super();

		this.setLayout(new FlowLayout());
		this.setTitle("==== Scan Ticket =====");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		final Dimension size = WebcamResolution.QVGA.getSize();

		this.webcam = Webcam.getWebcams().get(0);
		this.webcam.setViewSize(size);

		this.panel = new WebcamPanel(this.webcam);
		this.panel.setPreferredSize(size);

		this.add(this.panel);

		// Registering Parse Cloud Observer
		Controller.INSTANCE.addObserver(new ParseObserver());

		this.pack();
		this.setVisible(true);
		centreWindow(this);
		this.executor.execute(this);
	}

	/** {@inheritDoc}} */
	@Override
	public Thread newThread(final Runnable r) {
		final Thread t = new Thread(r, "ticket-scanner-worker");
		t.setDaemon(true);
		return t;
	}

	/** {@inheritDoc}} */
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
					// no QR code in image
				}
			}

			if (result != null) {
				final String ticketQrCode = result.getText();
				try {
					final Eticket eticket = parse(ticketQrCode);
					if ((eticket != null) && isValidTicket(eticket)) {
						// if ticket is not previously validated
						if (!isValidatedTicket(eticket.getTicketId())) {
							insertTicket(eticket);
							showMessageDialog(null, VALIDATED_MSG.getValue(), DIALOG_BOX_HEADER.getValue(),
									INFORMATION_MESSAGE);

							// Notify all the observers
							Controller.INSTANCE.getObservers().stream().forEach(observer -> observer.publish(eticket));

						} else {
							showMessageDialog(null, Constants.ALREADY_VALIDATED_MSG.getValue(),
									DIALOG_BOX_HEADER.getValue(), ERROR_MESSAGE);
						}

					}
				} catch (final NonParseableTicketException e) {
					showMessageDialog(null, INVALID_TICKET_MSG.getValue(), DIALOG_BOX_HEADER.getValue(), ERROR_MESSAGE);
				}
			}

		} while (true);
	}
}
