import java.util.*;

/**
 * MMU using enchanced second chance replacement strategy
 */

public class ClockMMU implements MMU {
    // variables
    int frames;
    int reads = 0;
    int writes = 0;
    int faults = 0;
    int pointer = 0;
    boolean debugMode = false;

    // list to store pages in memory along with their reference bits and dirty bits
    private ArrayList<PageFrame> pageList;

    // Constructors
    public ClockMMU(int frames) {
        // todo
        this.frames = frames;
        this.pageList = new ArrayList<>();
        for (int i = 0; i < frames; i++) {
            pageList.add(new PageFrame());
        }
    }

    public void setDebug() {
        // todo
        this.debugMode = true;
    }

    public void resetDebug() {
        // todo
        this.debugMode = false;
    }

    public void readMemory(int page_number) {

        String mode = "Reading";

        // Page is in memory
        for (int i = 0; i < pageList.size(); i++) {
            if (pageList.get(i).pageNumber == page_number) {
                pageList.get(i).ref = true;

                if (mode == "Writing") {
                    pageList.get(i).dirty = true;
                }
                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "reading", page_number));
                }

                return;
            }
        }

        // Page is not in memory

        faults += 1;
        reads += 1;

        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "Page fault", page_number));
        }
        // start clock algorithm
        while (true) {
            if (pageList.get(pointer).ref == false) {
                PageFrame replacePage = pageList.get(pointer);
                if (replacePage.dirty) {
                    writes += 1;
                    if (debugMode) {
                        System.out.println(String.format("%-15s %-10s", "Disk write", replacePage.pageNumber));
                    }
                }

                replacePage.pageNumber = page_number;
                replacePage.ref = true;
                replacePage.dirty = mode.equals("Writing");

                pointer = (pointer + 1) % frames;

                return;

            } else {
                pageList.get(pointer).ref = false;
                pointer = (pointer + 1) % frames;
            }
        }
    }

    public void writeMemory(int page_number) {

        String mode = "Writing";

        // Page is in memory
        for (int i = 0; i < pageList.size(); i++) {
            if (pageList.get(i).pageNumber == page_number) {
                pageList.get(i).ref = true;

                if (mode == "Writing") {
                    pageList.get(i).dirty = true;
                }
                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "Writing", page_number));
                }

                return;
            }
        }

        // Page is not in memory
        faults += 1;
        reads += 1;

        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "Page fault", page_number));
        }

        // Page is not in memory, start clock algorithm
        while (true) {
            if (pageList.get(pointer).ref == false) {
                PageFrame replacePage = pageList.get(pointer);
                if (replacePage.dirty) {
                    writes += 1;
                    if (debugMode) {
                        System.out.println(String.format("%-15s %-10s", "Disk write", replacePage.pageNumber));
                    }
                }

                replacePage.pageNumber = page_number;
                replacePage.ref = true;
                replacePage.dirty = mode.equals("Writing");

                pointer = (pointer + 1) % frames;

                return;

            } else {
                pageList.get(pointer).ref = false;
                pointer = (pointer + 1) % frames;
            }
        }
    }

    public int getTotalDiskReads() {
        // todo
        return reads;
    }

    public int getTotalDiskWrites() {
        // todo
        return writes;
    }

    public int getTotalPageFaults() {
        return faults;
    }

    // Method to explicitly discard a page from memory
    private void discard(PageFrame pageFrame) {
        if (pageList.contains(pageFrame)) {
            pageList.remove(pageFrame);
            if (debugMode) {
                System.out.println(String.format("%-15s %-10s", "Discard", pageFrame.pageNumber));
            }
        }
    }

    public class PageFrame {
        int pageNumber = -1;
        boolean dirty = false;
        boolean ref = false;

        public PageFrame() {
        }

        public PageFrame(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PageFrame other = (PageFrame) obj;
            return pageNumber == other.pageNumber;
        }
    }
}
