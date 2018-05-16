public class CurrentWallsInformCommand extends Command{
        private int wallsCount;

        public CurrentWallsInformCommand(int number){
            super(CommandCode.CURRENT_WALLS_INFORM);
            this.wallsCount = number;
        }

        public int getWallsCount()
        {
            return this.wallsCount;
        }

        public void setWallsCount(int number)
        {
            this.wallsCount = number;
        }
}

