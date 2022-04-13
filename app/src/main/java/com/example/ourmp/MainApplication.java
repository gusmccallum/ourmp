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
        MPID temp = allMPIds.get(allMPIds.indexOf(mpName));
        return temp.getMPID();
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

        Amplify.DataStore.clear(
                () -> Log.i("OurMP", "DataStore is cleared."),
                failure -> Log.e("OurMP", "Failed to clear DataStore."));

        allMPIds.add(new MPID("Ziad Aboultaif","89156"));
        allMPIds.add(new MPID("Scott Aitchison","105340"));
        allMPIds.add(new MPID("Dan Albas","72029"));
        allMPIds.add(new MPID("John Aldag","89258"));
        allMPIds.add(new MPID("Omar Alghabra","89535"));
        allMPIds.add(new MPID("Shafqat Ali","110339"));
        allMPIds.add(new MPID("Dean Allison","25446"));
        allMPIds.add(new MPID("Anita Anand","96081"));
        allMPIds.add(new MPID("Gary Anandasangaree","89449"));
        allMPIds.add(new MPID("Charlie Angus","25470"));
        allMPIds.add(new MPID("Mel Arnold","89294"));
        allMPIds.add(new MPID("René Arseneault","89383"));
        allMPIds.add(new MPID("Chandra Arya","88860"));
        allMPIds.add(new MPID("Niki Ashton","36037"));
        allMPIds.add(new MPID("Jenica Atwin","104586"));
        allMPIds.add(new MPID("Taylor Bachrach","105854"));
        allMPIds.add(new MPID("Vance Badawey","88867"));
        allMPIds.add(new MPID("Parm Bains","111067"));
        allMPIds.add(new MPID("Yvan Baker","105121"));
        allMPIds.add(new MPID("Tony Baldinelli","30330"));
        allMPIds.add(new MPID("John Barlow","86261"));
        allMPIds.add(new MPID("Michael Barrett","102275"));
        allMPIds.add(new MPID("Lisa Marie Barron","111023"));
        allMPIds.add(new MPID("Xavier Barsalou-Duval","88422"));
        allMPIds.add(new MPID("Jaime Battiste","104571"));
        allMPIds.add(new MPID("Mario Beaulieu","376"));
        allMPIds.add(new MPID("Terry Beech","89236"));
        allMPIds.add(new MPID("Rachel Bendayan","88567"));
        allMPIds.add(new MPID("Carolyn Bennett","992"));
        allMPIds.add(new MPID("Bob Benzen","96361"));
        allMPIds.add(new MPID("Candice Bergen","59110"));
        allMPIds.add(new MPID("Stéphane Bergeron","496"));
        allMPIds.add(new MPID("Luc Berthold","88541"));
        allMPIds.add(new MPID("Sylvie Bérubé","104622"));
        allMPIds.add(new MPID("James Bezan","25475"));
        allMPIds.add(new MPID("Marie-Claude Bibeau","88449"));
        allMPIds.add(new MPID("Chris Bittle","88934"));
        allMPIds.add(new MPID("Daniel Blaikie","89032"));
        allMPIds.add(new MPID("Bill Blair","88961"));
        allMPIds.add(new MPID("Yves-François Blanchet","104669"));
        allMPIds.add(new MPID("Maxime Blanchette-Joncas","104705"));
        allMPIds.add(new MPID("Rachel Blaney","89354"));
        allMPIds.add(new MPID("Kelly Block","59156"));
        allMPIds.add(new MPID("Kody Blois","104555"));
        allMPIds.add(new MPID("Randy Boissonnault","89147"));
        allMPIds.add(new MPID("Alexandre Boulerice","58775"));
        allMPIds.add(new MPID("Valerie Bradford","110487"));
        allMPIds.add(new MPID("Richard Bragdon","88369"));
        allMPIds.add(new MPID("John Brassard","88674"));
        allMPIds.add(new MPID("Élisabeth Brière","104977"));
        allMPIds.add(new MPID("Larry Brock","110354"));
        allMPIds.add(new MPID("Alexis Brunelle-Duceppe","104786"));
        allMPIds.add(new MPID("Blaine Calkins","35897"));
        allMPIds.add(new MPID("Richard Cannings","89327"));
        allMPIds.add(new MPID("Frank Caputo","111007"));
        allMPIds.add(new MPID("Jim Carr","89059"));
        allMPIds.add(new MPID("Colin Carrie","25486"));
        allMPIds.add(new MPID("Sean Casey","71270"));
        allMPIds.add(new MPID("Louise Chabot","104678"));
        allMPIds.add(new MPID("Bardish Chagger","89000"));
        allMPIds.add(new MPID("George Chahal","110882"));
        allMPIds.add(new MPID("Adam Chambers","110649"));
        allMPIds.add(new MPID("François-Philippe Champagne","88633"));
        allMPIds.add(new MPID("Martin Champoux","104741"));
        allMPIds.add(new MPID("Sophie Chatel","110225"));
        allMPIds.add(new MPID("Shaun Chen","88953"));
        allMPIds.add(new MPID("Paul Chiang","110513"));
        allMPIds.add(new MPID("Michael Chong","25488"));
        allMPIds.add(new MPID("Chad Collins","110438"));
        allMPIds.add(new MPID("Laurel Collins","105908"));
        allMPIds.add(new MPID("Michael Cooper","89219"));
        allMPIds.add(new MPID("Serge Cormier","88350"));
        allMPIds.add(new MPID("Michael Coteau","110373"));
        allMPIds.add(new MPID("Julie Dabrusin","88994"));
        allMPIds.add(new MPID("Marc Dalton","35909"));
        allMPIds.add(new MPID("Pam Damoff","88884"));
        allMPIds.add(new MPID("Raquel Dancho","105521"));
        allMPIds.add(new MPID("Scot Davidson","102653"));
        allMPIds.add(new MPID("Don Davies","59325"));
        allMPIds.add(new MPID("Claude DeBellefeuille","35315"));
        allMPIds.add(new MPID("Gérard Deltell","88535"));
        allMPIds.add(new MPID("Chris d'Entremont","49344"));
        allMPIds.add(new MPID("Caroline Desbiens","104715"));
        allMPIds.add(new MPID("Luc Desilets","104922"));
        allMPIds.add(new MPID("Blake Desjarlais","110889"));
        allMPIds.add(new MPID("Sukh Dhaliwal","31098"));
        allMPIds.add(new MPID("Anju Dhillon","88453"));
        allMPIds.add(new MPID("Lena Metlege Diab","109915"));
        allMPIds.add(new MPID("Todd Doherty","89249"));
        allMPIds.add(new MPID("Han Dong","105091"));
        allMPIds.add(new MPID("Terry Dowdall","105410"));
        allMPIds.add(new MPID("Earl Dreeshen","59226"));
        allMPIds.add(new MPID("Francis Drouin","88756"));
        allMPIds.add(new MPID("Emmanuel Dubourg","84660"));
        allMPIds.add(new MPID("Jean-Yves Duclos","89408"));
        allMPIds.add(new MPID("Terry Duguid","31119"));
        allMPIds.add(new MPID("Eric Duncan","105422"));
        allMPIds.add(new MPID("Kirsty Duncan","58877"));
        allMPIds.add(new MPID("Julie Dzerowicz","88721"));
        allMPIds.add(new MPID("Ali Ehsassi","89010"));
        allMPIds.add(new MPID("Fayçal El-Khoury","88515"));
        allMPIds.add(new MPID("Stephen Ellis","109905"));
        allMPIds.add(new MPID("Dave Epp","105082"));
        allMPIds.add(new MPID("Nathaniel Erskine-Smith","88687"));
        allMPIds.add(new MPID("Rosemarie Falk","98749"));
        allMPIds.add(new MPID("Ted Falk","84672"));
        allMPIds.add(new MPID("Ed Fast","35904"));
        allMPIds.add(new MPID("Greg Fergus","88478"));
        allMPIds.add(new MPID("Michelle Ferreri","110604"));
        allMPIds.add(new MPID("Andy Fillmore","88325"));
        allMPIds.add(new MPID("Kerry-Lynne Findlay","20878"));
        allMPIds.add(new MPID("Darren Fisher","88323"));
        allMPIds.add(new MPID("Peter Fonseca","71692"));
        allMPIds.add(new MPID("Mona Fortier","96356"));
        allMPIds.add(new MPID("Rhéal Fortin","88605"));
        allMPIds.add(new MPID("Peter Fragiskatos","88827"));
        allMPIds.add(new MPID("Sean Fraser","88316"));
        allMPIds.add(new MPID("Chrystia Freeland","84665"));
        allMPIds.add(new MPID("Hedy Fry","1589"));
        allMPIds.add(new MPID("Iqwinder Gaheer","110534"));
        allMPIds.add(new MPID("Cheryl Gallant","1809"));
        allMPIds.add(new MPID("Marc Garneau","10524"));
        allMPIds.add(new MPID("Jean-Denis Garon","110189"));
        allMPIds.add(new MPID("Randall Garrison","71995"));
        allMPIds.add(new MPID("Marie-Hélène Gaudreau","104806"));
        allMPIds.add(new MPID("Leah Gazan","87121"));
        allMPIds.add(new MPID("Bernard Généreux","63908"));
        allMPIds.add(new MPID("Garnett Genuis","89226"));
        allMPIds.add(new MPID("Mark Gerretsen","88802"));
        allMPIds.add(new MPID("Marilène Gill","88538"));
        allMPIds.add(new MPID("Marilyn Gladu","88938"));
        allMPIds.add(new MPID("Joël Godin","89407"));
        allMPIds.add(new MPID("Laila Goodridge","110918"));
        allMPIds.add(new MPID("Karina Gould","88715"));
        allMPIds.add(new MPID("Jacques Gourde","35397"));
        allMPIds.add(new MPID("Tracy Gray","105802"));
        allMPIds.add(new MPID("Matthew Green","93023"));
        allMPIds.add(new MPID("Steven Guilbeault","14171"));
        allMPIds.add(new MPID("Patty Hajdu","88984"));
        allMPIds.add(new MPID("Jasraj Singh Hallan","105630"));
        allMPIds.add(new MPID("Brendan Hanley","111109"));
        allMPIds.add(new MPID("Ken Hardie","89274"));
        allMPIds.add(new MPID("Lisa Hepfner","110446"));
        allMPIds.add(new MPID("Randy Hoback","59148"));
        allMPIds.add(new MPID("Mark Holland","25508"));
        allMPIds.add(new MPID("Anthony Housefather","88558"));
        allMPIds.add(new MPID("Carol Hughes","31289"));
        allMPIds.add(new MPID("Ahmed Hussen","89020"));
        allMPIds.add(new MPID("Gudie Hutchings","88292"));
        allMPIds.add(new MPID("Angelo Iacono","71337"));
        allMPIds.add(new MPID("Lori Idlout","111116"));
        allMPIds.add(new MPID("Marci Ien","107097"));
        allMPIds.add(new MPID("Helena Jaczek","105229"));
        allMPIds.add(new MPID("Matt Jeneroux","89167"));
        allMPIds.add(new MPID("Gord Johns","89263"));
        allMPIds.add(new MPID("Mélanie Joly","88384"));
        allMPIds.add(new MPID("Yvonne Jones","13218"));
        allMPIds.add(new MPID("Majid Jowhari","88929"));
        allMPIds.add(new MPID("Peter Julian","16399"));
        allMPIds.add(new MPID("Arielle Kayabaga","110502"));
        allMPIds.add(new MPID("Mike Kelloway","104531"));
        allMPIds.add(new MPID("Pat Kelly","89130"));
        allMPIds.add(new MPID("Iqra Khalid","88849"));
        allMPIds.add(new MPID("Kamal Khera","88705"));
        allMPIds.add(new MPID("Robert Kitchen","89095"));
        allMPIds.add(new MPID("Tom Kmiec","89136"));
        allMPIds.add(new MPID("Annie Koutrakis","105009"));
        allMPIds.add(new MPID("Michael Kram","89080"));
        allMPIds.add(new MPID("Shelby Kramp-Neuman","110454"));
        allMPIds.add(new MPID("Damien Kurek","105614"));
        allMPIds.add(new MPID("Stephanie Kusie","96367"));
        allMPIds.add(new MPID("Irek Kusmierczyk","71820"));
        allMPIds.add(new MPID("Jenny Kwan","89346"));
        allMPIds.add(new MPID("Mike Lake","35857"));
        allMPIds.add(new MPID("Marie-France Lalonde","92209"));
        allMPIds.add(new MPID("Emmanuella Lambropoulos","96350"));
        allMPIds.add(new MPID("David Lametti","88501"));
        allMPIds.add(new MPID("Kevin Lamoureux","30552"));
        allMPIds.add(new MPID("Melissa Lantsman","110665"));
        allMPIds.add(new MPID("Viviane Lapointe","110663"));
        allMPIds.add(new MPID("Andréanne Larouche","104973"));
        allMPIds.add(new MPID("Patricia Lattanzio","104957"));
        allMPIds.add(new MPID("Stéphane Lauzon","88394"));
        allMPIds.add(new MPID("Philip Lawrence","105291"));
        allMPIds.add(new MPID("Dominic LeBlanc","1813"));
        allMPIds.add(new MPID("Diane Lebouthillier","88460"));
        allMPIds.add(new MPID("Richard Lehoux","104653"));
        allMPIds.add(new MPID("Sébastien Lemire","104630"));
        allMPIds.add(new MPID("Chris Lewis","105120"));
        allMPIds.add(new MPID("Leslyn Lewis","88958"));
        allMPIds.add(new MPID("Ron Liepert","89139"));
        allMPIds.add(new MPID("Joël Lightbound","88532"));
        allMPIds.add(new MPID("Dane Lloyd","98079"));
        allMPIds.add(new MPID("Ben Lobb","35600"));
        allMPIds.add(new MPID("Wayne Long","88368"));
        allMPIds.add(new MPID("Lloyd Longfield","88761"));
        allMPIds.add(new MPID("Tim Louis","88810"));
        allMPIds.add(new MPID("Lawrence MacAulay","33"));
        allMPIds.add(new MPID("Heath MacDonald","109891"));
        allMPIds.add(new MPID("Alistair MacGregor","89269"));
        allMPIds.add(new MPID("Dave MacKenzie","891"));
        allMPIds.add(new MPID("Steven MacKinnon","88468"));
        allMPIds.add(new MPID("Larry Maguire","7251"));
        allMPIds.add(new MPID("James Maloney","88748"));
        allMPIds.add(new MPID("Richard Martel","100521"));
        allMPIds.add(new MPID("Soraya Martinez Ferrada","104756"));
        allMPIds.add(new MPID("Brian Masse","9137"));
        allMPIds.add(new MPID("Lindsay Mathyssen","105221"));
        allMPIds.add(new MPID("Bryan May","71599"));
        allMPIds.add(new MPID("Elizabeth May","2897"));
        allMPIds.add(new MPID("Dan Mazier","3306"));
        allMPIds.add(new MPID("Kelly McCauley","89179"));
        allMPIds.add(new MPID("Ken McDonald","88283"));
        allMPIds.add(new MPID("David McGuinty","9486"));
        allMPIds.add(new MPID("John McKay","957"));
        allMPIds.add(new MPID("Ron McKinnon","59293"));
        allMPIds.add(new MPID("Greg McLean","105623"));
        allMPIds.add(new MPID("Michael McLeod","89374"));
        allMPIds.add(new MPID("Heather McPherson","105689"));
        allMPIds.add(new MPID("Eric Melillo","105186"));
        allMPIds.add(new MPID("Alexandra Mendès","58621"));
        allMPIds.add(new MPID("Marco Mendicino","88738"));
        allMPIds.add(new MPID("Wilson Miao","111048"));
        allMPIds.add(new MPID("Kristina Michaud","104648"));
        allMPIds.add(new MPID("Marc Miller","88660"));
        allMPIds.add(new MPID("Rob Moore","17210"));
        allMPIds.add(new MPID("Marty Morantz","105511"));
        allMPIds.add(new MPID("Mike Morrice","110476"));
        allMPIds.add(new MPID("Rob Morrison","105807"));
        allMPIds.add(new MPID("Robert Morrissey","88308"));
        allMPIds.add(new MPID("Glen Motz","94305"));
        allMPIds.add(new MPID("Joyce Murray","35950"));
        allMPIds.add(new MPID("Dan Muys","110415"));
        allMPIds.add(new MPID("Yasir Naqvi","110572"));
        allMPIds.add(new MPID("John Nater","88917"));
        allMPIds.add(new MPID("Mary Ng","96352"));
        allMPIds.add(new MPID("Taleeb Noormohamed","72023"));
        allMPIds.add(new MPID("Christine Normandin","104947"));
        allMPIds.add(new MPID("Jennifer O'Connell","88925"));
        allMPIds.add(new MPID("Robert Oliphant","58858"));
        allMPIds.add(new MPID("Seamus O'Regan","88299"));
        allMPIds.add(new MPID("Erin O'Toole","72773"));
        allMPIds.add(new MPID("Jeremy Patzer","105559"));
        allMPIds.add(new MPID("Pierre Paul-Hus","71454"));
        allMPIds.add(new MPID("Monique Pauzé","88595"));
        allMPIds.add(new MPID("Rick Perkins","109922"));
        allMPIds.add(new MPID("Yves Perron","88418"));
        allMPIds.add(new MPID("Ginette Petitpas Taylor","88364"));
        allMPIds.add(new MPID("Louis Plamondon","413"));
        allMPIds.add(new MPID("Pierre Poilievre","25524"));
        allMPIds.add(new MPID("Marcus Powlowski","105437"));
        allMPIds.add(new MPID("Carla Qualtrough","89272"));
        allMPIds.add(new MPID("Alain Rayes","88600"));
        allMPIds.add(new MPID("Brad Redekopp","105598"));
        allMPIds.add(new MPID("Scott Reid","1827"));
        allMPIds.add(new MPID("Michelle Rempel Garner","71902"));
        allMPIds.add(new MPID("Blake Richards","59235"));
        allMPIds.add(new MPID("Anna Roberts","105191"));
        allMPIds.add(new MPID("Yves Robillard","88617"));
        allMPIds.add(new MPID("Pablo Rodriguez","25451"));
        allMPIds.add(new MPID("Churence Rogers","98744"));
        allMPIds.add(new MPID("Sherry Romanado","88521"));
        allMPIds.add(new MPID("Lianne Rood","105210"));
        allMPIds.add(new MPID("Anthony Rota","25452"));
        allMPIds.add(new MPID("Alex Ruff","105070"));
        allMPIds.add(new MPID("Ruby Sahota","88698"));
        allMPIds.add(new MPID("Harjit S. Sajjan","89497"));
        allMPIds.add(new MPID("Ya'ara Saks","107099"));
        allMPIds.add(new MPID("Darrell Samson","88333"));
        allMPIds.add(new MPID("Randeep Sarai","89339"));
        allMPIds.add(new MPID("Simon-Pierre Savard-Tremblay","104944"));
        allMPIds.add(new MPID("Francis Scarpaleggia","25453"));
        allMPIds.add(new MPID("Andrew Scheer","25454"));
        allMPIds.add(new MPID("Peter Schiefke","88649"));
        allMPIds.add(new MPID("Jamie Schmale","88770"));
        allMPIds.add(new MPID("Kyle Seeback","58841"));
        allMPIds.add(new MPID("Marc Serré","88874"));
        allMPIds.add(new MPID("Judy A. Sgro","1787"));
        allMPIds.add(new MPID("Brenda Shanahan","88442"));
        allMPIds.add(new MPID("Terry Sheehan","88944"));
        allMPIds.add(new MPID("Martin Shields","89109"));
        allMPIds.add(new MPID("Doug Shipley","105031"));
        allMPIds.add(new MPID("Maninder Sidhu","105045"));
        allMPIds.add(new MPID("Sonia Sidhu","88703"));
        allMPIds.add(new MPID("Mario Simard","104773"));
        allMPIds.add(new MPID("Nathalie Sinclair-Desgagné","110300"));
        allMPIds.add(new MPID("Jagmeet Singh","71588"));
        allMPIds.add(new MPID("Clifford Small","109867"));
        allMPIds.add(new MPID("Francesco Sorbara","88999"));
        allMPIds.add(new MPID("Gerald Soroka","105751"));
        allMPIds.add(new MPID("Sven Spengemann","88852"));
        allMPIds.add(new MPID("Warren Steinley","105581"));
        allMPIds.add(new MPID("Gabriel Ste-Marie","88485"));
        allMPIds.add(new MPID("Jake Stewart","109951"));
        allMPIds.add(new MPID("Pascale St-Onge","110044"));
        allMPIds.add(new MPID("Mark Strahl","71986"));
        allMPIds.add(new MPID("Shannon Stubbs","89198"));
        allMPIds.add(new MPID("Jenna Sudds","110459"));
        allMPIds.add(new MPID("Filomena Tassi","88784"));
        allMPIds.add(new MPID("Leah Taylor Roy","105024"));
        allMPIds.add(new MPID("Luc Thériault","88552"));
        allMPIds.add(new MPID("Alain Therrien","104783"));
        allMPIds.add(new MPID("Rachael Thomas","89200"));
        allMPIds.add(new MPID("Joanne Thompson","109877"));
        allMPIds.add(new MPID("Corey Tochor","84882"));
        allMPIds.add(new MPID("Fraser Tolmie","110800"));
        allMPIds.add(new MPID("Justin Trudeau","58733"));
        allMPIds.add(new MPID("Denis Trudel","88530"));
        allMPIds.add(new MPID("Ryan Turnbull","105480"));
        allMPIds.add(new MPID("Tim Uppal","30645"));
        allMPIds.add(new MPID("Rechie Valdez","110538"));
        allMPIds.add(new MPID("Tony Van Bynen","105270"));
        allMPIds.add(new MPID("Adam van Koeverden","105242"));
        allMPIds.add(new MPID("Tako Van Popta","105811"));
        allMPIds.add(new MPID("Dan Vandal","89045"));
        allMPIds.add(new MPID("Anita Vandenbeld","71738"));
        allMPIds.add(new MPID("Karen Vecchio","88742"));
        allMPIds.add(new MPID("Gary Vidal","105562"));
        allMPIds.add(new MPID("Dominique Vien","110009"));
        allMPIds.add(new MPID("Arnold Viersen","89211"));
        allMPIds.add(new MPID("Julie Vignola","104656"));
        allMPIds.add(new MPID("René Villemure","110306"));
        allMPIds.add(new MPID("Arif Virani","88910"));
        allMPIds.add(new MPID("Brad Vis","89289"));
        allMPIds.add(new MPID("Kevin Vuong","110654"));
        allMPIds.add(new MPID("Cathay Wagantall","89098"));
        allMPIds.add(new MPID("Chris Warkentin","35886"));
        allMPIds.add(new MPID("Kevin Waugh","89084"));
        allMPIds.add(new MPID("Len Webber","89116"));
        allMPIds.add(new MPID("Patrick Weiler","105918"));
        allMPIds.add(new MPID("Jonathan Wilkinson","89300"));
        allMPIds.add(new MPID("Ryan Williams","110330"));
        allMPIds.add(new MPID("John Williamson","71323"));
        allMPIds.add(new MPID("Jean Yip","98747"));
        allMPIds.add(new MPID("Salma Zahid","88950"));
        allMPIds.add(new MPID("Bonita Zarrillo","105837"));
        allMPIds.add(new MPID("Bob Zimmer","72035"));
        allMPIds.add(new MPID("Sameer Zuberi","54157"));


    }
}
