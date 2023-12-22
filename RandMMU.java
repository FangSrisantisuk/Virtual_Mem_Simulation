import java.util.*;

/**
 * MMU using random selection replacement strategy
 */

public class RandMMU implements MMU {

    // variables
    int frames;
    int reads = 0;
    int writes = 0;
    int faults = 0;
    boolean debugMode = false;

    // list to store pages in memory
    ArrayList<PageFrame> pageList;

    public RandMMU(int frames) {

        this.frames = frames;
        this.pageList = new ArrayList<>(frames);

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

        // todo
        PageFrame target = new PageFrame(page_number);
        if (pageList.contains(target) == false) {

            // if pagelist does not contain the page increase faults and reads
            faults += 1;
            reads += 1;

            if (debugMode) {
                System.out.println(String.format("%-15s %-10s", "Page fault", page_number));
            }
            // if memory is full, remove random page in list
            if (pageList.size() == frames) {

                // remove random page and save page int for debug
                Random random = new Random();
                int discardPageIndex = random.nextInt(pageList.size());
                PageFrame discardPageFrame = pageList.remove(discardPageIndex);
                boolean isDirty = discardPageFrame.dirty;

                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "Discard", discardPageFrame.pageNumber));
                }

                if (isDirty) {
                    writes += 1;
                    if (debugMode) {
                        System.out.println(String.format("%-15s %-10s", "Disk write", discardPageFrame.pageNumber));
                    }
                }
            }

        } else {
            ArrayList<PageFrame> newPageList = new ArrayList<>();

            for (PageFrame page : pageList) {
                if (page.pageNumber != page_number) {
                    newPageList.add(page);
                }
            }
            // Update pageList to contain the filtered elements
            pageList = newPageList;
        }

        PageFrame p = new PageFrame(page_number);
        pageList.add(p);

        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "reading", page_number));
        }
    }

    public void writeMemory(int page_number) {
        // todo
        PageFrame target = new PageFrame(page_number);
        if (pageList.contains(target) == false) {

            // if pagelist does not contain the page increase faults and reads
            faults += 1;
            reads += 1;

            if (debugMode) {
                System.out.println(String.format("%-15s %-10s", "Page fault", page_number));
            }
            // if memory is full, remove random page in list
            if (pageList.size() == frames) {

                // remove random page and save page int for debug
                Random random = new Random();
                int discardPageIndex = random.nextInt(pageList.size());
                PageFrame discardPageFrame = pageList.remove(discardPageIndex);
                boolean isDirty = discardPageFrame.dirty;

                if (debugMode) {
                    System.out.println(String.format("%-15s %-10s", "Discard", discardPageFrame.pageNumber));
                }

                if (isDirty) {
                    writes += 1;
                    if (debugMode) {
                        System.out.println(String.format("%-15s %-10s", "Disk write", discardPageFrame.pageNumber));
                    }
                }
            }

        } else {
            ArrayList<PageFrame> newPageList = new ArrayList<>();

            for (PageFrame page : pageList) {
                if (page.pageNumber != page_number) {
                    newPageList.add(page);
                }
            }
            // Update pageList to contain the filtered elements
            pageList = newPageList;
        }

        PageFrame p = new PageFrame(page_number);
        p.dirty = true;
        pageList.add(p);

        if (debugMode) {
            System.out.println(String.format("%-15s %-10s", "writing", page_number));
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

    public class PageFrame {
        int pageNumber = 0;
        boolean dirty = false;
        boolean ref = false;

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