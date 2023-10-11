package at.ac.fhcampuswien.model;

public class GameSettings {
    private int mines;
    private int height;
    private int width;

    public GameSettings(int mines, int height, int width) {
        this.mines = mines;
        this.height = height;
        this.width = width;
    }

    public GameSettings() {
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSettings that = (GameSettings) o;

        if (getMines() != that.getMines()) return false;
        if (getHeight() != that.getHeight()) return false;
        return getWidth() == that.getWidth();
    }

    @Override
    public int hashCode() {
        int result = getMines();
        result = 31 * result + getHeight();
        result = 31 * result + getWidth();
        return result;
    }
}
