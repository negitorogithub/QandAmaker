package unifar.unifar.qandamaker;

import java.util.Comparator;

class CustomComparatorByResult implements Comparator<Question> {

    @Override
    public int compare(Question q1 ,Question q2) {
        int correct[] = {0,0};
        Question question[] = {q1,q2};
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 3; i++) {
                if (question[j].getResults()[i]) {
                    correct[j]++;
                }
            }
        }
        return correct[0]-correct[1];
    }
}
