class OXOController
{

    private OXOModel model;
    private int turnCounter;

    public OXOController(OXOModel model)
    {
        this.model = model;
        turnCounter = 0;

        this.model.setCurrentPlayer(model.getPlayerByNumber(turnCounter));
    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException, CellAlreadyTakenException, CellDoesNotExistException
    {
       if (!ValidCommand(command)) throw new InvalidCellIdentifierException("type",command);

        int row = Character.getNumericValue(command.charAt(0)) - Character.getNumericValue('a');
        int col = Character.getNumericValue(command.charAt(1)) - 1;
        if(!ValidCellRange(row,col)) throw new CellDoesNotExistException(row, col); //Should I pass row+1 and col+1? i.e. do we expect the user to count from 0?
        if(!CellIsEmpty(row,col)) throw new CellAlreadyTakenException(row,col); //Same question as above

        model.setCellOwner(row,col,model.getCurrentPlayer());

        if (Win(row,col)) model.setWinner(model.getCurrentPlayer());
        if(Draw()) model.setGameDrawn();
        setNextPlayer();
    }

    private boolean ValidCommand(String command)
    {
        if (command.length()!=2) return false;
        if (!Character.isLetter(command.charAt(0))) return false;
        if (!Character.isDigit(command.charAt(1))) return false;
        return true;
    }

    //Do I pass row and col? Don't need to but feels wrong not to
    private boolean ValidCellRange(int row, int col)
    {
        if (row >= model.getNumberOfRows()) return false;
        if (col >= model.getNumberOfColumns()) return false;
        return true;
    }

    private boolean CellIsEmpty(int row, int col)
    {
       return (model.getCellOwner(row,col) == null);
    }

    private void setNextPlayer()
    {
        if (model.getCurrentPlayer() != model.getPlayerByNumber(model.getNumberOfPlayers()-1)) {
            turnCounter++;
        } else {
            turnCounter = 0;
        }
        model.setCurrentPlayer(model.getPlayerByNumber(turnCounter));
    }

    /*Algorithm inspired from https://stackoverflow.com/questions/1056316/algorithm-for-determining-tic-tac-toe-game-over
    Expanded to be more flexible to multiple players / larger board sizes / non-square board sizes*

    *in that case the diagonal checks are pretty bad and need to be improved
     */
    private boolean Win(int row, int col)
    {
        int matches;
        int squareSize;

        //Check Rows
        matches = 0;
        for (int i = 0; i < model.getNumberOfRows(); i++) {
            if (model.getRow(row).get(i) == model.getCurrentPlayer()) {
                if (++matches == model.getWinThreshold()) return true;
            } else {
                matches = 0;
            }
        }

        //Check Columns
        matches = 0;
        for (int i = 0; i < model.getNumberOfColumns(); i++) {
            if (model.getRow(i).get(col) == model.getCurrentPlayer()) {
                if (++matches == model.getWinThreshold()) return true;
            } else {
                matches = 0;
            }
        }

        //Check Diagonal
        squareSize = Math.min(model.getNumberOfColumns(),model.getNumberOfRows());
        matches = 0;
        if (col == row) {
            for (int i = 0; i < squareSize; i++) {
                if (model.getRow(i).get(i) == model.getCurrentPlayer()) {
                    if (++matches == model.getWinThreshold()) return true;
                } else {
                    matches = 0;
                }
            }
        }

        //Check Anti-Diagonal
        matches = 0;
        if (col+row == squareSize-1) {
            for (int i = 0; i < squareSize; i++) {
                if (model.getRow(i).get((squareSize-1)-i) == model.getCurrentPlayer()) {
                    if (++matches == model.getWinThreshold()) return true;
                } else {
                    matches = 0;
                }
            }
        }
        return false;
    }

    private boolean Draw()
    {
        for (int i = 0; i < model.getNumberOfRows(); i++) {
            for (int j = 0; j < model.getNumberOfColumns(); j++) {
                if (model.getCellOwner(i,j)==null) return false;
            }
        }

        return true;
    }
}
