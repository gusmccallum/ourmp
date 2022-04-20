package com.example.ourmp;

import static io.realm.Realm.getApplicationContext;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainApplication extends Application {



    private String appID = "ourmp-ksaww";
    private App app;
    private boolean isLoggedIn = false;
    public App getRealmApp() {
        return app;
    }

    public ArrayList<MPID> allMPIds = new ArrayList<MPID>();



    public String getMPId(String mpName) {
        for (int i = 0; i < allMPIds.size(); i++) {
            if (allMPIds.get(i).getMPName().equals(mpName)) {
                return allMPIds.get(i).getMPID();
            }
        }
        return "";
    }

    public String getMPParty(String mpName) {
        for (int i = 0; i < allMPIds.size(); i++) {
            if (allMPIds.get(i).getMPName().equals(mpName)) {
                return allMPIds.get(i).getMPParty();
            }
        }
        return "";
    }

    public ArrayList<MP> allMPs = new ArrayList<>();
    private DBManager dbManager = new DBManager();
    public DBManager getDbManager(){return dbManager;}

    public  NetworkingService getNetworkingService(){
        return networkingService;
    }
    private NetworkingService networkingService = new NetworkingService();

    private JsonService jsonService = new JsonService();
    public  JsonService getJsonService() {
        return jsonService;
    }

    public ArrayList<String> searchHistoryMPs = new ArrayList<>();

    public void setLogInStatus(boolean status) {
        isLoggedIn = status;
    }

    public boolean getLogInStatus() {
        return isLoggedIn;
    }

    public void logOut() {
        app.currentUser().logOutAsync(it -> {
            if (it.isSuccess()) {
                Toast.makeText(getApplicationContext(), "Logged Out.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Error logging out: " + it.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        isLoggedIn = false;
    }

    public String formatName(String fullName, String replacement){

        String formattedStr;

        if(fullName.equals("Robert J. Morrissey")){
            formattedStr = "Bobby Morrissey";
        }
        else if(fullName.equals("Candice Bergen")){
            formattedStr = "Candice Hoeppner";
        }
        else{

            formattedStr = fullName;
            if(fullName.equals("Harjit S. Sajjan")){
                formattedStr = "Harjit S Sajjan";
            }
            //change all non-enlgish letter to english
            formattedStr = formattedStr.replace("\u00e9", "e")
                    .replace("\u00e8", "e")
                    .replace("\u00e7", "c")
                    .replace("\u00c9", "e")
                    .replace("\u00eb", "e");
            //remove all '
            formattedStr = formattedStr.replace("'", "");
            //remove middle name with dot(.)
            int dot = formattedStr.indexOf(".");
            if(dot > -1){
                //ex - Michael V. McLeod, dot=9
                String s2 = formattedStr.substring(dot+1); //" McLeod"
                String s1 = formattedStr.substring(0, dot-2); // "Michael"
                formattedStr = s1+s2; //"Michael McLeod"
            }
        }

        //replace white space with replacement - or %20
        if(formattedStr.contains(" ")) {
            String[] splitStr = formattedStr.trim().split("\\s+");
            //ex) Adam, van, Koeverden
            String str="";
            for(int i=0; i<splitStr.length; i++){ //3
                if(i == splitStr.length-1){
                    str += splitStr[i]; //str = Adam-van-Koeverdeny
                }
                else{
                    str += splitStr[i]+replacement; //str = Adam-van-
                }
            }
            formattedStr = str;
        }

        return formattedStr;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        // Initialize Amplify

        try {
            Amplify.addPlugin(new AWSApiPlugin()); // UNCOMMENT this line once backend is deployed
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Amplify", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("Amplify", "Could not initialize Amplify", error);
        }
        app = new App(new AppConfiguration.Builder(appID).build());

        /*
        Amplify.DataStore.clear(
                () -> Log.i("OurMP", "DataStore is cleared."),
                failure -> Log.e("OurMP", "Failed to clear DataStore.")); */
//

        allMPIds.add(new MPID("Ziad Aboultaif","89156","Conservative"));
        allMPIds.add(new MPID("Scott Aitchison","105340","Conservative"));
        allMPIds.add(new MPID("Dan Albas","72029","Conservative"));
        allMPIds.add(new MPID("John Aldag","89258","Liberal"));
        allMPIds.add(new MPID("Omar Alghabra","89535","Liberal"));
        allMPIds.add(new MPID("Shafqat Ali","110339","Liberal"));
        allMPIds.add(new MPID("Dean Allison","25446","Conservative"));
        allMPIds.add(new MPID("Anita Anand","96081","Liberal"));
        allMPIds.add(new MPID("Gary Anandasangaree","89449","Liberal"));
        allMPIds.add(new MPID("Charlie Angus","25470","NDP"));
        allMPIds.add(new MPID("Mel Arnold","89294","Conservative"));
        allMPIds.add(new MPID("René Arseneault","89383","Liberal"));
        allMPIds.add(new MPID("Chandra Arya","88860","Liberal"));
        allMPIds.add(new MPID("Niki Ashton","36037","NDP"));
        allMPIds.add(new MPID("Jenica Atwin","104586","Liberal"));
        allMPIds.add(new MPID("Taylor Bachrach","105854","NDP"));
        allMPIds.add(new MPID("Vance Badawey","88867","Liberal"));
        allMPIds.add(new MPID("Parm Bains","111067","Liberal"));
        allMPIds.add(new MPID("Yvan Baker","105121","Liberal"));
        allMPIds.add(new MPID("Tony Baldinelli","30330","Conservative"));
        allMPIds.add(new MPID("John Barlow","86261","Conservative"));
        allMPIds.add(new MPID("Michael Barrett","102275","Conservative"));
        allMPIds.add(new MPID("Lisa Marie Barron","111023","NDP"));
        allMPIds.add(new MPID("Xavier Barsalou-Duval","88422","Bloc Québécois"));
        allMPIds.add(new MPID("Jaime Battiste","104571","Liberal"));
        allMPIds.add(new MPID("Mario Beaulieu","376","Bloc Québécois"));
        allMPIds.add(new MPID("Terry Beech","89236","Liberal"));
        allMPIds.add(new MPID("Rachel Bendayan","88567","Liberal"));
        allMPIds.add(new MPID("Carolyn Bennett","992","Liberal"));
        allMPIds.add(new MPID("Bob Benzen","96361","Conservative"));
        allMPIds.add(new MPID("Candice Bergen","59110","Conservative"));
        allMPIds.add(new MPID("Stéphane Bergeron","496","Bloc Québécois"));
        allMPIds.add(new MPID("Luc Berthold","88541","Conservative"));
        allMPIds.add(new MPID("Sylvie Bérubé","104622","Bloc Québécois"));
        allMPIds.add(new MPID("James Bezan","25475","Conservative"));
        allMPIds.add(new MPID("Marie-Claude Bibeau","88449","Liberal"));
        allMPIds.add(new MPID("Chris Bittle","88934","Liberal"));
        allMPIds.add(new MPID("Daniel Blaikie","89032","NDP"));
        allMPIds.add(new MPID("Bill Blair","88961","Liberal"));
        allMPIds.add(new MPID("Yves-François Blanchet","104669","Bloc Québécois"));
        allMPIds.add(new MPID("Maxime Blanchette-Joncas","104705","Bloc Québécois"));
        allMPIds.add(new MPID("Rachel Blaney","89354","NDP"));
        allMPIds.add(new MPID("Kelly Block","59156","Conservative"));
        allMPIds.add(new MPID("Kody Blois","104555","Liberal"));
        allMPIds.add(new MPID("Randy Boissonnault","89147","Liberal"));
        allMPIds.add(new MPID("Alexandre Boulerice","58775","NDP"));
        allMPIds.add(new MPID("Valerie Bradford","110487","Liberal"));
        allMPIds.add(new MPID("Richard Bragdon","88369","Conservative"));
        allMPIds.add(new MPID("John Brassard","88674","Conservative"));
        allMPIds.add(new MPID("Élisabeth Brière","104977","Liberal"));
        allMPIds.add(new MPID("Larry Brock","110354","Conservative"));
        allMPIds.add(new MPID("Alexis Brunelle-Duceppe","104786","Bloc Québécois"));
        allMPIds.add(new MPID("Blaine Calkins","35897","Conservative"));
        allMPIds.add(new MPID("Richard Cannings","89327","NDP"));
        allMPIds.add(new MPID("Frank Caputo","111007","Conservative"));
        allMPIds.add(new MPID("Jim Carr","89059","Liberal"));
        allMPIds.add(new MPID("Colin Carrie","25486","Conservative"));
        allMPIds.add(new MPID("Sean Casey","71270","Liberal"));
        allMPIds.add(new MPID("Louise Chabot","104678","Bloc Québécois"));
        allMPIds.add(new MPID("Bardish Chagger","89000","Liberal"));
        allMPIds.add(new MPID("George Chahal","110882","Liberal"));
        allMPIds.add(new MPID("Adam Chambers","110649","Conservative"));
        allMPIds.add(new MPID("François-Philippe Champagne","88633","Liberal"));
        allMPIds.add(new MPID("Martin Champoux","104741","Bloc Québécois"));
        allMPIds.add(new MPID("Sophie Chatel","110225","Liberal"));
        allMPIds.add(new MPID("Shaun Chen","88953","Liberal"));
        allMPIds.add(new MPID("Paul Chiang","110513","Liberal"));
        allMPIds.add(new MPID("Michael Chong","25488","Conservative"));
        allMPIds.add(new MPID("Chad Collins","110438","Liberal"));
        allMPIds.add(new MPID("Laurel Collins","105908","NDP"));
        allMPIds.add(new MPID("Michael Cooper","89219","Conservative"));
        allMPIds.add(new MPID("Serge Cormier","88350","Liberal"));
        allMPIds.add(new MPID("Michael Coteau","110373","Liberal"));
        allMPIds.add(new MPID("Julie Dabrusin","88994","Liberal"));
        allMPIds.add(new MPID("Marc Dalton","35909","Conservative"));
        allMPIds.add(new MPID("Pam Damoff","88884","Liberal"));
        allMPIds.add(new MPID("Raquel Dancho","105521","Conservative"));
        allMPIds.add(new MPID("Scot Davidson","102653","Conservative"));
        allMPIds.add(new MPID("Don Davies","59325","NDP"));
        allMPIds.add(new MPID("Claude DeBellefeuille","35315","Bloc Québécois"));
        allMPIds.add(new MPID("Gérard Deltell","88535","Conservative"));
        allMPIds.add(new MPID("Chris d'Entremont","49344","Conservative"));
        allMPIds.add(new MPID("Caroline Desbiens","104715","Bloc Québécois"));
        allMPIds.add(new MPID("Luc Desilets","104922","Bloc Québécois"));
        allMPIds.add(new MPID("Blake Desjarlais","110889","NDP"));
        allMPIds.add(new MPID("Sukh Dhaliwal","31098","Liberal"));
        allMPIds.add(new MPID("Anju Dhillon","88453","Liberal"));
        allMPIds.add(new MPID("Lena Metlege Diab","109915","Liberal"));
        allMPIds.add(new MPID("Todd Doherty","89249","Conservative"));
        allMPIds.add(new MPID("Han Dong","105091","Liberal"));
        allMPIds.add(new MPID("Terry Dowdall","105410","Conservative"));
        allMPIds.add(new MPID("Earl Dreeshen","59226","Conservative"));
        allMPIds.add(new MPID("Francis Drouin","88756","Liberal"));
        allMPIds.add(new MPID("Emmanuel Dubourg","84660","Liberal"));
        allMPIds.add(new MPID("Jean-Yves Duclos","89408","Liberal"));
        allMPIds.add(new MPID("Terry Duguid","31119","Liberal"));
        allMPIds.add(new MPID("Eric Duncan","105422","Conservative"));
        allMPIds.add(new MPID("Kirsty Duncan","58877","Liberal"));
        allMPIds.add(new MPID("Julie Dzerowicz","88721","Liberal"));
        allMPIds.add(new MPID("Ali Ehsassi","89010","Liberal"));
        allMPIds.add(new MPID("Fayçal El-Khoury","88515","Liberal"));
        allMPIds.add(new MPID("Stephen Ellis","109905","Conservative"));
        allMPIds.add(new MPID("Dave Epp","105082","Conservative"));
        allMPIds.add(new MPID("Nathaniel Erskine-Smith","88687","Liberal"));
        allMPIds.add(new MPID("Rosemarie Falk","98749","Conservative"));
        allMPIds.add(new MPID("Ted Falk","84672","Conservative"));
        allMPIds.add(new MPID("Ed Fast","35904","Conservative"));
        allMPIds.add(new MPID("Greg Fergus","88478","Liberal"));
        allMPIds.add(new MPID("Michelle Ferreri","110604","Conservative"));
        allMPIds.add(new MPID("Andy Fillmore","88325","Liberal"));
        allMPIds.add(new MPID("Kerry-Lynne Findlay","20878","Conservative"));
        allMPIds.add(new MPID("Darren Fisher","88323","Liberal"));
        allMPIds.add(new MPID("Peter Fonseca","71692","Liberal"));
        allMPIds.add(new MPID("Mona Fortier","96356","Liberal"));
        allMPIds.add(new MPID("Rhéal Fortin","88605","Bloc Québécois"));
        allMPIds.add(new MPID("Peter Fragiskatos","88827","Liberal"));
        allMPIds.add(new MPID("Sean Fraser","88316","Liberal"));
        allMPIds.add(new MPID("Chrystia Freeland","84665","Liberal"));
        allMPIds.add(new MPID("Hedy Fry","1589","Liberal"));
        allMPIds.add(new MPID("Iqwinder Gaheer","110534","Liberal"));
        allMPIds.add(new MPID("Cheryl Gallant","1809","Conservative"));
        allMPIds.add(new MPID("Marc Garneau","10524","Liberal"));
        allMPIds.add(new MPID("Jean-Denis Garon","110189","Bloc Québécois"));
        allMPIds.add(new MPID("Randall Garrison","71995","NDP"));
        allMPIds.add(new MPID("Marie-Hélène Gaudreau","104806","Bloc Québécois"));
        allMPIds.add(new MPID("Leah Gazan","87121","NDP"));
        allMPIds.add(new MPID("Bernard Généreux","63908","Conservative"));
        allMPIds.add(new MPID("Garnett Genuis","89226","Conservative"));
        allMPIds.add(new MPID("Mark Gerretsen","88802","Liberal"));
        allMPIds.add(new MPID("Marilène Gill","88538","Bloc Québécois"));
        allMPIds.add(new MPID("Marilyn Gladu","88938","Conservative"));
        allMPIds.add(new MPID("Joël Godin","89407","Conservative"));
        allMPIds.add(new MPID("Laila Goodridge","110918","Conservative"));
        allMPIds.add(new MPID("Karina Gould","88715","Liberal"));
        allMPIds.add(new MPID("Jacques Gourde","35397","Conservative"));
        allMPIds.add(new MPID("Tracy Gray","105802","Conservative"));
        allMPIds.add(new MPID("Matthew Green","93023","NDP"));
        allMPIds.add(new MPID("Steven Guilbeault","14171","Liberal"));
        allMPIds.add(new MPID("Patty Hajdu","88984","Liberal"));
        allMPIds.add(new MPID("Jasraj Singh Hallan","105630","Conservative"));
        allMPIds.add(new MPID("Brendan Hanley","111109","Liberal"));
        allMPIds.add(new MPID("Ken Hardie","89274","Liberal"));
        allMPIds.add(new MPID("Lisa Hepfner","110446","Liberal"));
        allMPIds.add(new MPID("Randy Hoback","59148","Conservative"));
        allMPIds.add(new MPID("Mark Holland","25508","Liberal"));
        allMPIds.add(new MPID("Anthony Housefather","88558","Liberal"));
        allMPIds.add(new MPID("Carol Hughes","31289","NDP"));
        allMPIds.add(new MPID("Ahmed Hussen","89020","Liberal"));
        allMPIds.add(new MPID("Gudie Hutchings","88292","Liberal"));
        allMPIds.add(new MPID("Angelo Iacono","71337","Liberal"));
        allMPIds.add(new MPID("Lori Idlout","111116","NDP"));
        allMPIds.add(new MPID("Marci Ien","107097","Liberal"));
        allMPIds.add(new MPID("Helena Jaczek","105229","Liberal"));
        allMPIds.add(new MPID("Matt Jeneroux","89167","Conservative"));
        allMPIds.add(new MPID("Gord Johns","89263","NDP"));
        allMPIds.add(new MPID("Mélanie Joly","88384","Liberal"));
        allMPIds.add(new MPID("Yvonne Jones","13218","Liberal"));
        allMPIds.add(new MPID("Majid Jowhari","88929","Liberal"));
        allMPIds.add(new MPID("Peter Julian","16399","NDP"));
        allMPIds.add(new MPID("Arielle Kayabaga","110502","Liberal"));
        allMPIds.add(new MPID("Mike Kelloway","104531","Liberal"));
        allMPIds.add(new MPID("Pat Kelly","89130","Conservative"));
        allMPIds.add(new MPID("Iqra Khalid","88849","Liberal"));
        allMPIds.add(new MPID("Kamal Khera","88705","Liberal"));
        allMPIds.add(new MPID("Robert Kitchen","89095","Conservative"));
        allMPIds.add(new MPID("Tom Kmiec","89136","Conservative"));
        allMPIds.add(new MPID("Annie Koutrakis","105009","Liberal"));
        allMPIds.add(new MPID("Michael Kram","89080","Conservative"));
        allMPIds.add(new MPID("Shelby Kramp-Neuman","110454","Conservative"));
        allMPIds.add(new MPID("Damien Kurek","105614","Conservative"));
        allMPIds.add(new MPID("Stephanie Kusie","96367","Conservative"));
        allMPIds.add(new MPID("Irek Kusmierczyk","71820","Liberal"));
        allMPIds.add(new MPID("Jenny Kwan","89346","NDP"));
        allMPIds.add(new MPID("Mike Lake","35857","Conservative"));
        allMPIds.add(new MPID("Marie-France Lalonde","92209","Liberal"));
        allMPIds.add(new MPID("Emmanuella Lambropoulos","96350","Liberal"));
        allMPIds.add(new MPID("David Lametti","88501","Liberal"));
        allMPIds.add(new MPID("Kevin Lamoureux","30552","Liberal"));
        allMPIds.add(new MPID("Melissa Lantsman","110665","Conservative"));
        allMPIds.add(new MPID("Viviane Lapointe","110663","Liberal"));
        allMPIds.add(new MPID("Andréanne Larouche","104973","Bloc Québécois"));
        allMPIds.add(new MPID("Patricia Lattanzio","104957","Liberal"));
        allMPIds.add(new MPID("Stéphane Lauzon","88394","Liberal"));
        allMPIds.add(new MPID("Philip Lawrence","105291","Conservative"));
        allMPIds.add(new MPID("Dominic LeBlanc","1813","Liberal"));
        allMPIds.add(new MPID("Diane Lebouthillier","88460","Liberal"));
        allMPIds.add(new MPID("Richard Lehoux","104653","Conservative"));
        allMPIds.add(new MPID("Sébastien Lemire","104630","Bloc Québécois"));
        allMPIds.add(new MPID("Chris Lewis","105120","Conservative"));
        allMPIds.add(new MPID("Leslyn Lewis","88958","Conservative"));
        allMPIds.add(new MPID("Ron Liepert","89139","Conservative"));
        allMPIds.add(new MPID("Joël Lightbound","88532","Liberal"));
        allMPIds.add(new MPID("Dane Lloyd","98079","Conservative"));
        allMPIds.add(new MPID("Ben Lobb","35600","Conservative"));
        allMPIds.add(new MPID("Wayne Long","88368","Liberal"));
        allMPIds.add(new MPID("Lloyd Longfield","88761","Liberal"));
        allMPIds.add(new MPID("Tim Louis","88810","Liberal"));
        allMPIds.add(new MPID("Lawrence MacAulay","33","Liberal"));
        allMPIds.add(new MPID("Heath MacDonald","109891","Liberal"));
        allMPIds.add(new MPID("Alistair MacGregor","89269","NDP"));
        allMPIds.add(new MPID("Dave MacKenzie","891","Conservative"));
        allMPIds.add(new MPID("Steven MacKinnon","88468","Liberal"));
        allMPIds.add(new MPID("Larry Maguire","7251","Conservative"));
        allMPIds.add(new MPID("James Maloney","88748","Liberal"));
        allMPIds.add(new MPID("Richard Martel","100521","Conservative"));
        allMPIds.add(new MPID("Soraya Martinez Ferrada","104756","Liberal"));
        allMPIds.add(new MPID("Brian Masse","9137","NDP"));
        allMPIds.add(new MPID("Lindsay Mathyssen","105221","NDP"));
        allMPIds.add(new MPID("Bryan May","71599","Liberal"));
        allMPIds.add(new MPID("Elizabeth May","2897","Green Party"));
        allMPIds.add(new MPID("Dan Mazier","3306","Conservative"));
        allMPIds.add(new MPID("Kelly McCauley","89179","Conservative"));
        allMPIds.add(new MPID("Ken McDonald","88283","Liberal"));
        allMPIds.add(new MPID("David McGuinty","9486","Liberal"));
        allMPIds.add(new MPID("John McKay","957","Liberal"));
        allMPIds.add(new MPID("Ron McKinnon","59293","Liberal"));
        allMPIds.add(new MPID("Greg McLean","105623","Conservative"));
        allMPIds.add(new MPID("Michael McLeod","89374","Liberal"));
        allMPIds.add(new MPID("Heather McPherson","105689","NDP"));
        allMPIds.add(new MPID("Eric Melillo","105186","Conservative"));
        allMPIds.add(new MPID("Alexandra Mendès","58621","Liberal"));
        allMPIds.add(new MPID("Marco Mendicino","88738","Liberal"));
        allMPIds.add(new MPID("Wilson Miao","111048","Liberal"));
        allMPIds.add(new MPID("Kristina Michaud","104648","Bloc Québécois"));
        allMPIds.add(new MPID("Marc Miller","88660","Liberal"));
        allMPIds.add(new MPID("Rob Moore","17210","Conservative"));
        allMPIds.add(new MPID("Marty Morantz","105511","Conservative"));
        allMPIds.add(new MPID("Mike Morrice","110476","Green Party"));
        allMPIds.add(new MPID("Rob Morrison","105807","Conservative"));
        allMPIds.add(new MPID("Robert Morrissey","88308","Liberal"));
        allMPIds.add(new MPID("Glen Motz","94305","Conservative"));
        allMPIds.add(new MPID("Joyce Murray","35950","Liberal"));
        allMPIds.add(new MPID("Dan Muys","110415","Conservative"));
        allMPIds.add(new MPID("Yasir Naqvi","110572","Liberal"));
        allMPIds.add(new MPID("John Nater","88917","Conservative"));
        allMPIds.add(new MPID("Mary Ng","96352","Liberal"));
        allMPIds.add(new MPID("Taleeb Noormohamed","72023","Liberal"));
        allMPIds.add(new MPID("Christine Normandin","104947","Bloc Québécois"));
        allMPIds.add(new MPID("Jennifer O'Connell","88925","Liberal"));
        allMPIds.add(new MPID("Robert Oliphant","58858","Liberal"));
        allMPIds.add(new MPID("Seamus O'Regan","88299","Liberal"));
        allMPIds.add(new MPID("Erin O'Toole","72773","Conservative"));
        allMPIds.add(new MPID("Jeremy Patzer","105559","Conservative"));
        allMPIds.add(new MPID("Pierre Paul-Hus","71454","Conservative"));
        allMPIds.add(new MPID("Monique Pauzé","88595","Bloc Québécois"));
        allMPIds.add(new MPID("Rick Perkins","109922","Conservative"));
        allMPIds.add(new MPID("Yves Perron","88418","Bloc Québécois"));
        allMPIds.add(new MPID("Ginette Petitpas Taylor","88364","Liberal"));
        allMPIds.add(new MPID("Louis Plamondon","413","Bloc Québécois"));
        allMPIds.add(new MPID("Pierre Poilievre","25524","Conservative"));
        allMPIds.add(new MPID("Marcus Powlowski","105437","Liberal"));
        allMPIds.add(new MPID("Carla Qualtrough","89272","Liberal"));
        allMPIds.add(new MPID("Alain Rayes","88600","Conservative"));
        allMPIds.add(new MPID("Brad Redekopp","105598","Conservative"));
        allMPIds.add(new MPID("Scott Reid","1827","Conservative"));
        allMPIds.add(new MPID("Michelle Rempel Garner","71902","Conservative"));
        allMPIds.add(new MPID("Blake Richards","59235","Conservative"));
        allMPIds.add(new MPID("Anna Roberts","105191","Conservative"));
        allMPIds.add(new MPID("Yves Robillard","88617","Liberal"));
        allMPIds.add(new MPID("Pablo Rodriguez","25451","Liberal"));
        allMPIds.add(new MPID("Churence Rogers","98744","Liberal"));
        allMPIds.add(new MPID("Sherry Romanado","88521","Liberal"));
        allMPIds.add(new MPID("Lianne Rood","105210","Conservative"));
        allMPIds.add(new MPID("Anthony Rota","25452","Liberal"));
        allMPIds.add(new MPID("Alex Ruff","105070","Conservative"));
        allMPIds.add(new MPID("Ruby Sahota","88698","Liberal"));
        allMPIds.add(new MPID("Harjit S. Sajjan","89497","Liberal"));
        allMPIds.add(new MPID("Ya'ara Saks","107099","Liberal"));
        allMPIds.add(new MPID("Darrell Samson","88333","Liberal"));
        allMPIds.add(new MPID("Randeep Sarai","89339","Liberal"));
        allMPIds.add(new MPID("Simon-Pierre Savard-Tremblay","104944","Bloc Québécois"));
        allMPIds.add(new MPID("Francis Scarpaleggia","25453","Liberal"));
        allMPIds.add(new MPID("Andrew Scheer","25454","Conservative"));
        allMPIds.add(new MPID("Peter Schiefke","88649","Liberal"));
        allMPIds.add(new MPID("Jamie Schmale","88770","Conservative"));
        allMPIds.add(new MPID("Kyle Seeback","58841","Conservative"));
        allMPIds.add(new MPID("Marc Serré","88874","Liberal"));
        allMPIds.add(new MPID("Judy A. Sgro","1787","Liberal"));
        allMPIds.add(new MPID("Brenda Shanahan","88442","Liberal"));
        allMPIds.add(new MPID("Terry Sheehan","88944","Liberal"));
        allMPIds.add(new MPID("Martin Shields","89109","Conservative"));
        allMPIds.add(new MPID("Doug Shipley","105031","Conservative"));
        allMPIds.add(new MPID("Maninder Sidhu","105045","Liberal"));
        allMPIds.add(new MPID("Sonia Sidhu","88703","Liberal"));
        allMPIds.add(new MPID("Mario Simard","104773","Bloc Québécois"));
        allMPIds.add(new MPID("Nathalie Sinclair-Desgagné","110300","Bloc Québécois"));
        allMPIds.add(new MPID("Jagmeet Singh","71588","NDP"));
        allMPIds.add(new MPID("Clifford Small","109867","Conservative"));
        allMPIds.add(new MPID("Francesco Sorbara","88999","Liberal"));
        allMPIds.add(new MPID("Gerald Soroka","105751","Conservative"));
        allMPIds.add(new MPID("Sven Spengemann","88852","Liberal"));
        allMPIds.add(new MPID("Warren Steinley","105581","Conservative"));
        allMPIds.add(new MPID("Gabriel Ste-Marie","88485","Bloc Québécois"));
        allMPIds.add(new MPID("Jake Stewart","109951","Conservative"));
        allMPIds.add(new MPID("Pascale St-Onge","110044","Liberal"));
        allMPIds.add(new MPID("Mark Strahl","71986","Conservative"));
        allMPIds.add(new MPID("Shannon Stubbs","89198","Conservative"));
        allMPIds.add(new MPID("Jenna Sudds","110459","Liberal"));
        allMPIds.add(new MPID("Filomena Tassi","88784","Liberal"));
        allMPIds.add(new MPID("Leah Taylor Roy","105024","Liberal"));
        allMPIds.add(new MPID("Luc Thériault","88552","Bloc Québécois"));
        allMPIds.add(new MPID("Alain Therrien","104783","Bloc Québécois"));
        allMPIds.add(new MPID("Rachael Thomas","89200","Conservative"));
        allMPIds.add(new MPID("Joanne Thompson","109877","Liberal"));
        allMPIds.add(new MPID("Corey Tochor","84882","Conservative"));
        allMPIds.add(new MPID("Fraser Tolmie","110800","Conservative"));
        allMPIds.add(new MPID("Justin Trudeau","58733","Liberal"));
        allMPIds.add(new MPID("Denis Trudel","88530","Bloc Québécois"));
        allMPIds.add(new MPID("Ryan Turnbull","105480","Liberal"));
        allMPIds.add(new MPID("Tim Uppal","30645","Conservative"));
        allMPIds.add(new MPID("Rechie Valdez","110538","Liberal"));
        allMPIds.add(new MPID("Tony Van Bynen","105270","Liberal"));
        allMPIds.add(new MPID("Adam van Koeverden","105242","Liberal"));
        allMPIds.add(new MPID("Tako Van Popta","105811","Conservative"));
        allMPIds.add(new MPID("Dan Vandal","89045","Liberal"));
        allMPIds.add(new MPID("Anita Vandenbeld","71738","Liberal"));
        allMPIds.add(new MPID("Karen Vecchio","88742","Conservative"));
        allMPIds.add(new MPID("Gary Vidal","105562","Conservative"));
        allMPIds.add(new MPID("Dominique Vien","110009","Conservative"));
        allMPIds.add(new MPID("Arnold Viersen","89211","Conservative"));
        allMPIds.add(new MPID("Julie Vignola","104656","Bloc Québécois"));
        allMPIds.add(new MPID("René Villemure","110306","Bloc Québécois"));
        allMPIds.add(new MPID("Arif Virani","88910","Liberal"));
        allMPIds.add(new MPID("Brad Vis","89289","Conservative"));
        allMPIds.add(new MPID("Kevin Vuong","110654","Independent"));
        allMPIds.add(new MPID("Cathay Wagantall","89098","Conservative"));
        allMPIds.add(new MPID("Chris Warkentin","35886","Conservative"));
        allMPIds.add(new MPID("Kevin Waugh","89084","Conservative"));
        allMPIds.add(new MPID("Len Webber","89116","Conservative"));
        allMPIds.add(new MPID("Patrick Weiler","105918","Liberal"));
        allMPIds.add(new MPID("Jonathan Wilkinson","89300","Liberal"));
        allMPIds.add(new MPID("Ryan Williams","110330","Conservative"));
        allMPIds.add(new MPID("John Williamson","71323","Conservative"));
        allMPIds.add(new MPID("Jean Yip","98747","Liberal"));
        allMPIds.add(new MPID("Salma Zahid","88950","Liberal"));
        allMPIds.add(new MPID("Bonita Zarrillo","105837","NDP"));
        allMPIds.add(new MPID("Bob Zimmer","72035","Conservative"));
        allMPIds.add(new MPID("Sameer Zuberi","54157","Liberal"));
    }
}
