package ca.grimoire.formtree;

/**
 * Callbacks for handling the reconstituted form data.
 * 
 * @param <T>
 *            the type of the decoding result.
 */
public interface FormReceiver<T> extends FormElementReceiver {

    /**
     * Notifies the receiver that form decoding has finished.
     * 
     * @return the result of the form decoding process.
     */
    public T finished();

}
