package main.java.org.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Mouse extends MouseAdapter implements Observable {
    private final Board b;
    public Mouse(Board board){
        this.b=board;
        register(board);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x=e.getX(); //Returns the vizszintes x position of the event relative to the source component
        int y=e.getY();
        int sor=y/b.getCellSize(); //mivel vizszintesen a sorok 15onkent kovetik egymast, ha a relativ poziciot elosztjuk 15 el, es ez egy int, megkapjuk melyik sorban vagyunk
        int oszlop=x/b.getCellSize();


        if (!b.getinGame()) {
            b.newGame();
            b.repaint();
        }
        if(sor<b.getRows() && oszlop<b.getColumns()){
            if(e.getButton()==MouseEvent.BUTTON3){//elv right click, szoval ha jelolni akarunk
                if(b.getSpielfeld()[sor][oszlop]<=b.getCoveredMineCell() && b.getSpielfeld()[sor][oszlop]>b.getCellWithMine()){ //ha kisebb, akkor meg fix nincs marked-olva, csak covered
                    if(b.getMinesLeft()>0){
                        b.getSpielfeld()[sor][oszlop] += b.getMarkedCell();
                        b.setMinesLeft(-1);
                        reportToObservers();
                        //b.getText().setText(Integer.toString(b.getMinesLeft()));
                        b.repaint();
                    }
                    else{
                        reportToObservers();
                        //b.getText().setText(Integer.toString(b.getMinesLeft()));
                    }
                }
                else if(b.getSpielfeld()[sor][oszlop]>= b.getMarkedCell()){ //ha le akarjuk szedni a marke-ot
                    b.getSpielfeld()[sor][oszlop] -= b.getMarkedCell();
                    b.setMinesLeft(1);
                    reportToObservers();
                    //b.getText().setText(Integer.toString(b.getMinesLeft()));
                    b.repaint();
                }
            }
            else{ //ha ball click
                if (b.getSpielfeld()[sor][oszlop]==b.getCoveredMineCell()) {
                    b.getSpielfeld()[sor][oszlop] -= b.getCoveredCell();
                    b.setInGame(false);
                    b.setEnd(true);
                    b.repaint();
                }
                else if(b.getSpielfeld()[sor][oszlop]==b.getEmptyCell()+b.getCoveredCell()){
                    b.emptycells(sor, oszlop);
                    reportToObservers();
                    b.repaint();
                }
                else if(b.getSpielfeld()[sor][oszlop]>b.getCellWithMine() && b.getSpielfeld()[sor][oszlop]<b.getCoveredMineCell()){
                    b.getSpielfeld()[sor][oszlop] -= b.getCoveredCell();
                    b.repaint();
                }
            }
        }
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void reportToObservers() {
        for(int i=0; i<observers.size(); i++){
            observers.get(i).report(this);
        }
    }
}

