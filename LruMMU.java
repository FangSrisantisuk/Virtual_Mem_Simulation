import java.util.LinkedList;

public class LruMMU implements MMU {
    // LinkedList to save page data
    private LinkedList<Page> pageList;

    // variables
    int frames;
    int reads = 0;
    int writes = 0;
    int faults = 0;
    boolean debugMode = false;

    public LruMMU(int frames) {
        this.frames = frames;
        this.pageList = new LinkedList<>();
    }

    public void setDebug() {
        this.debugMode = true;
    }

    public void resetDebug() {
        this.debugMode = false;
    }

    public void readMemory(int page_number) {
        String mode = "Reading";

        for (int i = 0; i < pageList.size(); i++) {
            Page target = pageList.get(i);
            if (target.pageNumber == page_number) {
                pageList.add(pageList.remove(i));
                if (mode.equals("Writing")) {
                    target.isDirty = true;
                }
                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "reading", page_number));
                }
                return;
            }
        }

        faults += 1;
        reads += 1;
        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "Page fault", page_number));
        }

        if (pageList.size() == frames) {
            Page discardPageFramePage = pageList.removeFirst();
            boolean isDirty = discardPageFramePage.isDirty;

            if (debugMode) {
                System.out.println(String.format("%-15s %-10s", "Discard",
                        discardPageFramePage.pageNumber));
            }

            if (isDirty) {
                writes++;
                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "Disk write", discardPageFramePage.pageNumber));
                }
            }
        }

        pageList.add(new Page(page_number, mode.equals("Writing")));
        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "reading", page_number));
        }
    }

    public void writeMemory(int page_number) {

        String mode = "Writing";

        for (int i = 0; i < pageList.size(); i++) {
            Page entry = pageList.get(i);
            if (entry.pageNumber == page_number) {
                pageList.add(pageList.remove(i));
                if (mode.equals("Writing")) {
                    entry.isDirty = true;
                }
                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "reading", page_number));
                }
                return;
            }
        }

        faults += 1;
        reads += 1;
        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "Page fault", page_number));
        }

        if (pageList.size() == frames) {
            Page discardPageFramePage = pageList.removeFirst();
            boolean isDirty = discardPageFramePage.isDirty;

            if (debugMode) {
                System.out.println(String.format("%-15s %-10s", "Discard",
                        discardPageFramePage.pageNumber));
            }

            if (isDirty) {
                writes++;
                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "Disk write", discardPageFramePage.pageNumber));
                }
            }
        }

        pageList.add(new Page(page_number, mode.equals("Writing")));
        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "Writing", page_number));
        }
    }

    public int getTotalDiskReads() {
        return reads;
    }

    public int getTotalDiskWrites() {
        return writes;
    }

    public int getTotalPageFaults() {
        return faults;
    }
}

class Page {
    int pageNumber;
    boolean isDirty;

    public Page(int pageNumber, boolean isDirty) {
        this.pageNumber = pageNumber;
        this.isDirty = isDirty;
    }
}
