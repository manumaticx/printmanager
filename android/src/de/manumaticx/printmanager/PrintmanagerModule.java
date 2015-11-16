package de.manumaticx.printmanager;

import java.util.HashMap;
import java.io.IOException;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.io.TiFileFactory;
import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;

import android.content.Context;
import android.print.PrintManager;
import android.print.PrintDocumentAdapter;

@Kroll.module(name="Printmanager", id="de.manumaticx.printmanager")
public class PrintmanagerModule extends KrollModule
{

	private static final String LCAT = "PrintmanagerModule";
	private static final boolean DBG = TiConfig.LOGD;
	
	private static PrintManager pm;
	

	public PrintmanagerModule()
	{
		super();
	}
	
	private PrintManager getManager(){
		if (pm != null) {
			return pm;
		}

		pm = (PrintManager) TiApplication.getAppRootOrCurrentActivity()
				.getSystemService(Context.PRINT_SERVICE);
		return pm;
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		Log.d(LCAT, "inside onAppCreate");
	}
	
	public void print(Object args){
		
		String jobName = TiApplication.getInstance().getPackageName() + " Document";
		getManager().print(jobName, pda, null);
		
		HashMap<String, String> d = (HashMap<String, String>) args;
		
		if (!d.containsKey(TiC.PROPERTY_URL)){
			Log.e(LCAT,"url not provided");
			return;
		}
		
		try {
			// Load the image from the application assets
			String url = getPathToApplicationAsset(d.get(TiC.PROPERTY_URL));
			TiBaseFile file = TiFileFactory.createTitaniumFile(new String[] { url }, false);
			
		} catch (IOException e) {
			Log.e(LCAT,"File not found");
		}
		
		
		PrintDocumentAdapter pda = new PrintDocumentAdapter(){

		    @Override
		    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback){
		        InputStream input = null;
		        OutputStream output = null;

		        try {

		            input = new FileInputStream(file);
		            output = new FileOutputStream(destination.getFileDescriptor());

		            byte[] buf = new byte[1024];
		            int bytesRead;

		            while ((bytesRead = input.read(buf)) > 0) {
		                 output.write(buf, 0, bytesRead);
		            }

		            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

		        } catch (FileNotFoundException ee){
		            //Catch exception
		        } catch (Exception e) {
		            //Catch exception
		        } finally {
		            try {
		                input.close();
		                output.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }

		    @Override
		    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras){

		        if (cancellationSignal.isCanceled()) {
		            callback.onLayoutCancelled();
		            return;
		        }

		        int pages = computePageCount(newAttributes);

		        PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Name of file").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();

		        callback.onLayoutFinished(pdi, true);
		    }
		};
	}

}

