package com.ton.jacky.simon;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;


import static com.ton.jacky.simon.State.COMPUTER;
import static com.ton.jacky.simon.State.HUMAN;
import static com.ton.jacky.simon.State.LOSE;
import static com.ton.jacky.simon.State.START;
import static com.ton.jacky.simon.State.WIN;


/**
 * Created by Jacky on 11/25/2017.
 */


public class Model extends Observable {
    static final Model ourInstance = new Model();
    private int score;
    private int length;
    private int buttons;
    private int difficulty;
    private ArrayList<Integer> sequence = new ArrayList<Integer>();
    private int index;
    private State state;

    public void init(int _buttons, int _difficulty) {
        difficulty = _difficulty;
        length = 1;
        buttons = _buttons;
        state = LOSE;
        score = 0;
    }

    static Model getInstance() {
        return ourInstance;
    }

    public int getNumButtons() {
        return buttons;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setNumButtons(int _buttons) {buttons = _buttons;}

    public void setDifficulty(int _difficulty) {difficulty = _difficulty;}

    public int getScore() {
        return score;
    }

    public State getState(){
        return state;
    }

    public void newRound() {
        if (state == LOSE) {
            length = 1;
            score = 0;
        }
        sequence.clear();
        Random rand = new Random();
        for (int i = 0; i < length; ++i) {
            int b = rand.nextInt(buttons);
            sequence.add(b);
        }
        index = 0;
        state = COMPUTER;

    }

    public int nextButton() {
        int button = sequence.get(index);
        ++ index;
        if (index >= sequence.size()) {
            index = 0;
            state = HUMAN;
        }
        return button;
    }

    public boolean verifyButton(int button) {
        boolean correct = (button == sequence.get(index));
        index++;
        if (!correct) {
            state = LOSE;
        } else {
            if (index == sequence.size()) {
                state = WIN;
                score++;
                length++;
            }
        }
        return correct;
    }

    public void initObservers() {
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

}


