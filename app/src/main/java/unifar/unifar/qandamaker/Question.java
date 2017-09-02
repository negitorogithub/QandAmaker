package unifar.unifar.qandamaker;


class Question implements Cloneable{
    String questionName;
    String answerName;
    String tagName;
    Boolean results[];
    Boolean resultBuffer;



    @Override
    public Question clone()  {
        Question cloned = null;
        try {
            cloned = (Question)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }
}

