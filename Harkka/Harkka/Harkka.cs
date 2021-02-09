using System;
using System.Collections.Generic;
using Jypeli;
using Jypeli.Assets;
using Jypeli.Controls;
using Jypeli.Effects;
using Jypeli.Widgets;

/// @author Mikko Toitturi
/// @version 8.12.2015
/// <summary>
/// Peli, jossa astronautti yrittää päästä ehjänä raketilleen aikarajan sisällä.
/// </summary>
public class Harkka : PhysicsGame
{
    private const double NOPEUS = 200;
    private const double HYPPYNOPEUS = 600;
    private const double SUPERHYPPYNOPEUS = 760;
    private const int RUUDUN_KOKO = 40;

    private DoubleMeter alaspainLaskuri;
    private Timer tasonAika;
    private Timer meteoriAjastin;
    private IntMeter elamat;

    private PlatformCharacter pelaaja1;
    private Image pelaajanKuva = LoadImage("Ukko1");
    private SoundEffect maaliAani = LoadSoundEffect("maali");


    /// <summary>
    /// Aloittaa pelin aukaisemalla alkunäytön.
    /// </summary>
    public override void Begin()
    {
        AlkuNaytto();
    }


    /// <summary>
    /// Aloittaa varsinaisen pelin.
    /// </summary>
    public void AloitaPeli()
    {
        ClearAll();
        Gravity = new Vector(0, -500);

        LuoKentta();
        LisaaPelaaja();
        LisaaNappaimet();

        MeteoriittiAjastin();
        TasonAikaLaskuri();

        Camera.Follow(pelaaja1);
        Camera.ZoomFactor = 1.2;
        Camera.StayInLevel = true;
    }


    /// <summary>
    /// Luo Tilemapin mukaan asetetut palikat, tason rajat ja taustan.
    /// </summary>
    private void LuoKentta()
    {
        TileMap kentta = TileMap.FromLevelAsset("kentta1");
        kentta.SetTileMethod('#', LisaaTaso);
        kentta.SetTileMethod('R', LisaaRaketti);
        kentta.Execute(RUUDUN_KOKO, RUUDUN_KOKO);
        Level.CreateBorders();
        Level.Background.CreateStars(640);
    }


    /// <summary>
    /// Aukaisee alkunäytön, josta voi valita, aloittaako pelin, katsooko ohjeet pelaamiseen vai lopettaako.
    /// </summary>
    private void AlkuNaytto()
    {
        MultiSelectWindow AlkuValikko = new MultiSelectWindow("FASTRONAUT", "Aloita peli", "Ohjeet", "Lopeta peli");
        AlkuValikko.Color = Color.Crimson;
        Add(AlkuValikko);
        AlkuValikko.AddItemHandler(0, AloitaPeli);
        AlkuValikko.AddItemHandler(1, Ohjeet);
        AlkuValikko.AddItemHandler(2, Exit);
        Level.Background.CreateStars(640);
    }


    /// <summary>
    /// Ohjevalikko. Kertoo, mitä tehdä ja miten.
    /// </summary>
    private void Ohjeet()
    {
        Image ohjek1 = LoadImage("Ohjekuva");

        Label Nappaimet = LuoLabel(Screen.Top - 100, "Liiku, hyppää ja kumarru nuolinäppäimillä. Välilyönnistä hyppää vielä korkeammalle.");
        Add(Nappaimet);

        Label Liikkumisohje = LuoLabel(Screen.Top - 150, "Liiku oikealle päin päästäksesi rakettiin. Väistele meteoriiteja hyppäämällä niiden ylitse tai kumartumalla niiden alitse.");
        Add(Liikkumisohje);

        Label Elämäohje = LuoLabel(Screen.Top - 200, "Näet jäljellä olevat elämäsi näytön oikeasta yläkulmasta. Meteoriittiin osuminen vähentää niitä yhdellä.");
        Add(Elämäohje);

        Label Aikaohje = LuoLabel(Screen.Top - 250, "Sinulla on rajallinen aika päästä tason loppuun. Näet ajan näytön vasemmasta yläkulmasta. Jos aika tai elämät loppuu, peli päättyy.");
        Add(Aikaohje);

        Label ohjekuva = new Label(ohjek1.Width, ohjek1.Height);
        ohjekuva.Image = ohjek1;
        ohjekuva.Position = new Vector(Screen.Left + ohjekuva.Width/1.8, Aikaohje.Y - ohjek1.Height/1.5);
        Add(ohjekuva);

        MultiSelectWindow Valinta = new MultiSelectWindow("Mitäs sitten?", "Aloita peli", "Lopeta");
        Valinta.Position = new Vector(ohjekuva.X + ohjekuva.Width/1.3, ohjekuva.Y);
        Valinta.ItemSelected += UusiPeli;
        Add(Valinta); 
    }


    /// <summary>
    /// Luo ohjetekstit ohjevalikkoon.
    /// </summary>
    /// <param name="Y">Tekstin y-koordinaatti</param>
    /// <param name="teksti">Teksti</param>
    /// <returns>Ohjetekstin oikealle paikalle</returns>
    private static Label LuoLabel(double Y, string teksti)
    {
        Label UusiLabel = new Label(Screen.Width, 50);
        UusiLabel.Y = Y;
        UusiLabel.Text = teksti;
        UusiLabel.TextColor = Color.White;
        UusiLabel.BorderColor = Color.White;
        return UusiLabel;
    }


    /// <summary>
    /// Luo palan tason "lattiaa" tilemapin mukaan.
    /// </summary>
    /// <param name="paikka">Paikka, mihin pala sijoitetaan. Tilemapissa "#"</param>
    /// <param name="leveys">Palan leveys</param>
    /// <param name="korkeus">Palan korkeus</param>
    private void LisaaTaso(Vector paikka, double leveys, double korkeus)
    {
        PhysicsObject taso = PhysicsObject.CreateStaticObject(leveys, korkeus);
        taso.Position = paikka;
        taso.Image = LoadImage("Taso");
        Add(taso);
    }


    /// <summary>
    /// Lisää maalina toimivan raketin. Paikka tilemapista.
    /// </summary>
    /// <param name="paikka">Raketin paikka. Tilemapissa "R"</param>
    /// <param name="leveys">Raketin leveys</param>
    /// <param name="korkeus">Raketin korkeus</param>
    private void LisaaRaketti(Vector paikka, double leveys, double korkeus)
    {
        PhysicsObject raketti = PhysicsObject.CreateStaticObject(119.8, 200);
        raketti.Position = paikka;
        raketti.Image = LoadImage("Raketti");
        raketti.Tag = "raketti";
        Add(raketti);
    }


    /// <summary>
    /// Lisää tasoon ajastimen, joka laskee alaspäin 50:stä. Kun päässään nollaan, peli loppuu.
    /// </summary>
    public void TasonAikaLaskuri()
    {
        alaspainLaskuri = new DoubleMeter(50);
        tasonAika = new Timer();

        tasonAika.Interval = 0.1;
        tasonAika.Timeout += AikaLoppui;
        tasonAika.Start();

        Label AikaNaytto = new Label(100, 65);
        AikaNaytto.TextColor = Color.White;
        AikaNaytto.DecimalPlaces = 1;
        AikaNaytto.BindTo(alaspainLaskuri);
        AikaNaytto.X = Screen.Left + 50;
        AikaNaytto.Y = Screen.Top - 30;                               
        AikaNaytto.Font = Font.DefaultLarge;
        AikaNaytto.BorderColor = Color.BlueGray;
        Add(AikaNaytto);
    }


    /// <summary>
    /// Lopettaa pelin, kun ajastin pääsee nollaan.
    /// </summary>
    public void AikaLoppui()
    {
        alaspainLaskuri.Value -= 0.1;
        if (alaspainLaskuri.Value <= 0.0)
        {
            MessageDisplay.Add("Aika Loppui");
            GameOver("Aika loppui. Uusi peli?");
        }
    }


    /// <summary>
    /// Luo valintaikkunan, joka kertoo annetun arvon mukaan, voittiko vai hävisikö, ja antaa mahdollisuuden
    /// aloittaa uusi peli tai lopettaa peli.
    /// </summary>
    /// <param name="voititko">Viesti, joka kertoo pelaajalle, voittiko vai hävisikö</param>
    public void GameOver(string voititko)
    {
        tasonAika.Stop();
        meteoriAjastin.Stop();
        MultiSelectWindow GameOver = new MultiSelectWindow(voititko, "Uusi peli", "Lopeta");
        GameOver.ItemSelected += UusiPeli;
        Add(GameOver);
    }


    /// <summary>
    /// Aloittaa uuden pelin tai lopettaa pelin, kun käyttäjä on hävinnyt tai voittanut pelin.
    /// </summary>
    /// <param name="valinta">Käyttäjän valitsema nappi. "Uusi peli"(case 0) tai "Lopeta"(case 1)</param>
    private void UusiPeli(int valinta)
    {
        switch (valinta)
        {
            case 0:
                AloitaPeli();
                break;

            case 1:
                Exit();
                break;              
        }
    }


    /// <summary>
    /// Luo ajastimen, joka tietyin aikavälin luo meteoriitteja.
    /// </summary>
    public void MeteoriittiAjastin()
    {
        meteoriAjastin = new Timer();
        meteoriAjastin.Interval = 1.5;
        int luku = RandomGen.NextInt(1, 3);
        meteoriAjastin.Timeout += LuoMeteoriitti;
        meteoriAjastin.Start();
    }


    /// <summary>
    /// Luo meteoriitin ajastimen mukaan, ja arpoo sen y-koordinaatin.
    /// </summary>
    public void LuoMeteoriitti()
    {
        int luku = RandomGen.NextInt(1, 4);

        PhysicsObject meteoriitti = new PhysicsObject(50, 50);
        meteoriitti.Shape = Shape.Circle;
        meteoriitti.Image = LoadImage("Meteoriitti");
        meteoriitti.X = pelaaja1.X + 310;

        if (luku == 1) meteoriitti.Y = -35;
        if (luku == 2) meteoriitti.Y = 15;               
        if (luku == 3) meteoriitti.Y = 70;               // Y: Alin -35, Keskimmäinen n. 15, Ylin n. 70. X: P.X + 300

        meteoriitti.IgnoresGravity = true;
        meteoriitti.Mass = 100;
        meteoriitti.Tag = "meteoriitti";
        meteoriitti.LifetimeLeft = TimeSpan.FromSeconds(4.0);

        Vector vauhti = new Vector(-210.0, 0.0);
        meteoriitti.Move(vauhti);
        if(meteoriitti.X < 2220)Add(meteoriitti);      //Estää sen, että meteoriitti spawnaisi raketin taakse.
    }
                                              

    /// <summary>
    /// Lisää pelaajan peliin, laittaa näkyviin jäljellä olevat elämät.
    /// </summary>
    public void LisaaPelaaja()
    {
        pelaaja1 = new PlatformCharacter(34.3, 80);
        pelaaja1.Position = new Vector(-2540, -60);  // Saatu Tilemapista
        pelaaja1.Mass = 4.0;
        pelaaja1.Image = pelaajanKuva;
        AddCollisionHandler(pelaaja1, "meteoriitti", TormaaMeteoriittiin);
        AddCollisionHandler(pelaaja1, "raketti", TormaaRakettiin);
        Add(pelaaja1);

        elamat = new IntMeter(3);

        Label Life = new Label();
        Life.BindTo(elamat);
        Life.Font = Font.DefaultLargeBold;
        Life.TextColor = Color.White;
        Life.X = Screen.Right - 200;
        Life.Y = Screen.Top - 30;
        Life.BorderColor = Color.JungleGreen;
        Add(Life);

        Label Selitys = new Label("Elämää jäljellä");
        Selitys.X = Screen.Right - 100;
        Selitys.Y = Screen.Top - 30;
        Selitys.TextColor = Color.JungleGreen;
        Add(Selitys);
    }


    /// <summary>
    /// Lisää näppäimet liikkumista varten.
    /// </summary>
    private void LisaaNappaimet()
    {
        Keyboard.Listen(Key.F1, ButtonState.Pressed, ShowControlHelp, "Näytä ohjeet");
        Keyboard.Listen(Key.Escape, ButtonState.Pressed, ConfirmExit, "Lopeta peli");

        Keyboard.Listen(Key.Left, ButtonState.Down, Liikuta, "Liikkuu vasemmalle", pelaaja1, -NOPEUS);
        Keyboard.Listen(Key.Right, ButtonState.Down, Liikuta, "Liikkuu vasemmalle", pelaaja1, NOPEUS);
        Keyboard.Listen(Key.Up, ButtonState.Pressed, Hyppaa, "Hyppää", pelaaja1, HYPPYNOPEUS);
        Keyboard.Listen(Key.Down, ButtonState.Down, Kumarru, "Kumarru (paina pohjassa)", pelaaja1);
        Keyboard.Listen(Key.Down, ButtonState.Released, Nouse, "Nouse (päästä irti)", pelaaja1);
        Keyboard.Listen(Key.Space, ButtonState.Pressed, Hyppaa, "Hyppää korkeammalle", pelaaja1, SUPERHYPPYNOPEUS);
    }


    /// <summary>
    /// Laittaa pelaajan kumartumaan, kun näppäin on painettuna alas.
    /// </summary>
    /// <param name="hahmo"></param>
    private void Kumarru(PlatformCharacter hahmo)
    {
        hahmo.Height = 65;
        hahmo.Width = 60;
        hahmo.Y -= 3;
        hahmo.Image = LoadImage("KyykkyUkko");
    }


    /// <summary>
    /// Nostaa hahmon ylös, kun näppäin nostetaan.
    /// </summary>
    /// <param name="hahmo"></param>
    private void Nouse(PlatformCharacter hahmo)
    {
        hahmo.Height = 80;
        hahmo.Width = 34.3;
        hahmo.Image = pelaajanKuva;
    }


    /// <summary>
    /// Liikuttaa pelaajaa, kun pelaaja ei ole kumartunut.
    /// </summary>
    /// <param name="hahmo">Pelaajahahmo</param>
    /// <param name="nopeus">Liikkumisnopeus</param>
    private void Liikuta(PlatformCharacter hahmo, double nopeus)
    {
        if(pelaaja1.Image == pelaajanKuva)hahmo.Walk(nopeus);
    }


    /// <summary>
    /// Hyppää, kun näppäintä painetaan.
    /// </summary>
    /// <param name="hahmo">Pelaajahahmo</param>
    /// <param name="nopeus">Hyppynopeus. 2 näppäintä eli 2 mahdollisuutta.</param>
    private void Hyppaa(PlatformCharacter hahmo, double nopeus)
    {
        hahmo.Jump(nopeus);
    }


    /// <summary>
    /// Päättää pelin, kun pelaaja pääsee maaliin asti.
    /// </summary>
    /// <param name="hahmo">Pelaajahahmo</param>
    /// <param name="raketti">Maalina toimiva raketti</param>
    private void TormaaRakettiin(PhysicsObject hahmo, PhysicsObject raketti)
    {
        maaliAani.Play();
        double pisteet = PisteLaskuri();
        GameOver("Pääsit maaliin! Pisteesi ovat " + pisteet + ". Uusi Peli?");
    }


    /// <summary>
    /// Räjäyttää meteoriitin ja vähentää pelaajalta elämän, kun pelaaja osuu meteoriittiin.
    /// </summary>
    /// <param name="hahmo">Pelaaja</param>
    /// <param name="meteoriitti">Meteoriitti</param>
    private void TormaaMeteoriittiin(PhysicsObject hahmo, PhysicsObject meteoriitti)
    {
        meteoriitti.Destroy();
        Explosion Rajahdys = new Explosion(50);
        Rajahdys.Position = meteoriitti.Position;
        Add(Rajahdys);
        if (elamat.Value != 1) PlaySound("Osuma");

        elamat.Value--;                                       
        if (elamat.Value == 0)
        {
            pelaaja1.Destroy();
            GameOver("Kuolit. Uusi peli?");
        }
    }


    /// <summary>
    /// Laskee pisteet, kun pääsee maaliin. Pisteitä saadaan suoritusajasta & jäljellä olevista elämistä.
    /// </summary>
    /// <returns>Pisteet</returns>
    private double PisteLaskuri()
    {
        double pisteet = 0;
        double[] pistetaulukko = new double[] { (alaspainLaskuri * 100), (elamat.Value * 1000) };
        for (int i = 0; i < pistetaulukko.Length; i++)
        {
            pisteet += pistetaulukko[i];
        }
        pisteet = Math.Round(pisteet);
        return pisteet;
    }
}