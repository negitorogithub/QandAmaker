package unifar.unifar.qandamaker;


class Question implements Cloneable{
    private String questionName;
    private String answerName;
    private String tagName;
    private int corrects;
    private Boolean results[];
    private Boolean resultBuffer;


    private int index;


    Question(String questionName, String answerName, String tagName, Boolean[] results, int index){
        this.setQuestionName(questionName);
        this.setAnswerName(answerName);
        this.setTagName(tagName);
        this.setResults(results);
        this.setIndex(index);
    }

    Question() {
    }

    void resetAll(){
        this.setQuestionName(null);
        this.setAnswerName(null);
        this.setTagName(null);
        this.setResultBuffer(null);
        this.setResults(null);
        this.corrects = 0;
    }

    public Object getOBjectFromIndex(int index){
        Object objectToReturn = new Object();
        int remainder = index%MainActivity.INT_QfileLinesPerOneQuestion;
        switch (remainder){
            case MainActivity.INT_QfileQuestionIndex:
                objectToReturn = getQuestionName();
                break;
            case MainActivity.INT_QfileAnswerIndex:
                objectToReturn = getAnswerName();
                break;
            case MainActivity.INT_QfileTagIndex:
                objectToReturn = getTagName();
                break;
            case MainActivity.INT_QfileAnswerHistoryIndex1:
                objectToReturn = getResults()[0];
                break;
            case MainActivity.INT_QfileAnswerHistoryIndex2:
                objectToReturn = getResults()[1];
                break;
            case MainActivity.INT_QfileAnswerHistoryIndex3:
                objectToReturn = getResults()[2];
                break;
        }
        return objectToReturn;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getAnswerName() {
        return answerName;
    }

    public int getCorrects() {
        return corrects;
    }

    public void setCorrects(int corrects) {
        this.corrects = corrects;
    }

    public void setAnswerName(String answerName) {
        this.answerName = answerName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Boolean[] getResults() {
        return results;
    }

    public void setResults(Boolean[] results) {
        this.results = results;
        int correctBuffer = 0;
        if (this.results != null) {
            for (int i = 0; i < 3; i++) {
                if (this.results[i]) {
                    correctBuffer++;
                }
            }
        }

        this.setCorrects(correctBuffer);
    }

    public Boolean getResultBuffer() {
        return resultBuffer;
    }

    public void setResultBuffer(Boolean resultBuffer) {
        this.resultBuffer = resultBuffer;
    }




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

