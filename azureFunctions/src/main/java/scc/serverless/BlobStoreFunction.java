package scc.serverless;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

public class BlobStoreFunction {

    @FunctionName("blobReplication")
    public void replicate(@BlobTrigger(name = "blobReplication", dataType = "binary", path = "images/{name}", connection = "BlobStoreConnection") byte[] content,
                          @BindingName("name") String blobname,
                          final ExecutionContext context) {
        new AzureBlobReplicator("ReplicatedBlobStoreConnection").uploadMedia(blobname, content);
    }

    @FunctionName("blobReplication2")
    public void replicate2(@BlobTrigger(name = "blobReplication2", dataType = "binary", path = "images/{name}", connection = "ReplicatedBlobStoreConnection") byte[] content,
                          @BindingName("name") String blobname,
                          final ExecutionContext context) {
        new AzureBlobReplicator("BlobStoreConnection").uploadMedia(blobname, content);
    }

}
